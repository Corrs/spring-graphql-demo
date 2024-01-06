package com.yanxuan88.australiacallcenter.service.impl;

import com.wf.captcha.SpecCaptcha;
import com.yanxuan88.australiacallcenter.config.RedisClient;
import com.yanxuan88.australiacallcenter.event.model.SysLoginLogEvent;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.model.entity.SysLogLogin;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;
import com.yanxuan88.australiacallcenter.model.enums.LoginOperationEnum;
import com.yanxuan88.australiacallcenter.model.enums.LoginStatusEnum;
import com.yanxuan88.australiacallcenter.model.enums.UserStatusEnum;
import com.yanxuan88.australiacallcenter.model.vo.*;
import com.yanxuan88.australiacallcenter.service.ILoginService;
import com.yanxuan88.australiacallcenter.service.IMenuService;
import com.yanxuan88.australiacallcenter.service.IUserService;
import com.yanxuan88.australiacallcenter.util.JWTUtil;
import com.yanxuan88.australiacallcenter.util.RequestAttrUtil;
import com.yanxuan88.australiacallcenter.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yanxuan88.australiacallcenter.common.Constant.*;
import static com.yanxuan88.australiacallcenter.exception.BaseResultCodeEnum.*;

@Slf4j
@Service
public class LoginServiceImpl implements ILoginService, ApplicationEventPublisherAware {
    private static final String CACHE_LOGIN_CAPTCHA_PREFIX = "cache:loginCaptcha:";
    @Autowired
    private RedisClient redisClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;

    @Autowired
    private IMenuService menuService;

    private ApplicationEventPublisher publisher;

    @Override
    public LoginCaptchaVO createLoginCaptcha(String loginCaptchaKey) {
        SpecCaptcha specCaptcha = new SpecCaptcha(106, 37, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = StringUtils.hasText(loginCaptchaKey) ? loginCaptchaKey.trim() : UUID.randomUUID().toString();
        // 存入redis并设置过期时间为5分钟
        redisClient.set(CACHE_LOGIN_CAPTCHA_PREFIX.concat(key), verCode, 5, TimeUnit.MINUTES);
        // 将key和base64返回给前端
        return new LoginCaptchaVO(specCaptcha.toBase64(), key);
    }

    /**
     * 登录
     * 1. 校验验证码。如果key不存在，提示验证码过期，前端需要刷新验证码；如果验证码不匹配，提示验证码不正确
     * 2. 从sys_user查询用户数据。如果用户不存在，提示用户不存在；如果用户存在，密码不匹配，提示密码错误。
     * 3. 查询用户权限
     * 4. 删除验证码缓存，缓存用户登录信息，组装VO，返回给前端
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     * @return UserLoginInfoVO
     */
    @Override
    public UserLoginInfoVO login(String username, String password, String captcha) {
        String captchaKey = (String) RequestAttrUtil.getAttribute(HEADER_CAPTCHA_KEY);
        String ip = (String) RequestAttrUtil.getAttribute(IP);
        String userAgent = (String) RequestAttrUtil.getAttribute(HttpHeaders.USER_AGENT);
        log.info("用户登录，username={}，password={}，captcha={}，captchaKey={}，ip={}", username, password, captcha, captchaKey, ip);
        // 1.
        if (!StringUtils.hasText(captchaKey)) {
            throw new BizException(LOGIN_CAPTCHA_EXPIRE);
        }

        String captchaCacheKey = CACHE_LOGIN_CAPTCHA_PREFIX.concat(captchaKey);
        String captchaCache = redisClient.get(captchaCacheKey);
        if (!StringUtils.hasText(captchaCache)) {
            throw new BizException(LOGIN_CAPTCHA_EXPIRE);
        }

        if (!captcha.equalsIgnoreCase(captchaCache)) {
            throw new BizException(LOGIN_CAPTCHA_FAIL);
        }

        SysLogLogin logLogin = new SysLogLogin();
        logLogin.setOperation(LoginOperationEnum.LOGIN.getCode());
        logLogin.setCreateTime(LocalDateTime.now());
        logLogin.setIp(ip);
        logLogin.setUserAgent(userAgent);
        logLogin.setCreateName(username);
        // 2.
        SysUser user = userService.queryByUsername(username);
        // 用户不存在
        if (user == null) {
            logLogin.setStatus(LoginStatusEnum.FAIL.getCode());
            publisher.publishEvent(new SysLoginLogEvent(logLogin));
            throw new BizException(USER_NOT_FOUND);
        }

        logLogin.setCreateUser(user.getUserId());
        // 密码不正确
        if (!passwordEncoder.matches(SecurityUtil.pwd(user.getSalt(), password), user.getPassword())) {
            logLogin.setStatus(LoginStatusEnum.FAIL.getCode());
            publisher.publishEvent(new SysLoginLogEvent(logLogin));
            throw new BizException(PASSWORD_FAIL);
        }

        // 账号锁定
        if (user.getStatus() == UserStatusEnum.DISABLE.getCode()) {
            logLogin.setStatus(LoginStatusEnum.LOCK.getCode());
            publisher.publishEvent(new SysLoginLogEvent(logLogin));
            throw new BizException(ACCOUNT_DISABLE);
        }

        // 记录登录成功日志
        logLogin.setStatus(LoginStatusEnum.SUCCESS.getCode());
        publisher.publishEvent(new SysLoginLogEvent(logLogin));
        // 3.
        List<MenuVO> menus = menuService.menus(user.getUserId());
        // 4.
        redisClient.delete(captchaCacheKey);
        String uuid = UUID.randomUUID().toString();
        String authenticationToken = JWTUtil.generateToken(Collections.singletonMap(TOKEN_UUID, uuid));

        UserLoginInfoVO result = new UserLoginInfoVO()
                .setPermissions(menus.stream()
                        .map(e -> new UserPermissionVO()
                                .setId(e.getId())
                                .setParentId(e.getParentId())
                                .setIcon(e.getIcon())
                                .setPerms(e.getPerms())
                                .setType(e.getType())
                                .setSort(e.getSort())
                                .setName(e.getName())
                                .setUrl(e.getUrl()))
                        .collect(Collectors.toList()))
                .setUser(new UserBaseVO().setMobile(user.getMobile())
                        .setEmail(user.getEmail())
                        .setUsername(user.getUsername())
                        .setRealName(user.getRealName())
                        .setAvatar(user.getAvatar())
                        .setDeptId(user.getDeptId())
                        .setSuperAdmin(user.getSuperAdmin())
                        .setUserId(user.getUserId()));
        redisClient.set(SESSION_KEY.concat(uuid), result, SESSION_EXPIRE, SESSION_EXPIRE_UNIT);
        return result.setAuthenticationToken(authenticationToken);
    }

    @Override
    public boolean logout() {
        Optional.ofNullable(SecurityUtil.getUserLoginInfo())
                .ifPresent(user -> {
                    String ip = (String) RequestAttrUtil.getAttribute(IP);
                    String userAgent = (String) RequestAttrUtil.getAttribute(HttpHeaders.USER_AGENT);
                    SysLogLogin logLogin = new SysLogLogin();
                    logLogin.setOperation(LoginOperationEnum.LOGOUT.getCode());
                    logLogin.setIp(ip);
                    logLogin.setUserAgent(userAgent);
                    logLogin.setStatus(LoginStatusEnum.SUCCESS.getCode());
                    logLogin.setCreateUser(user.getUserId());
                    logLogin.setCreateName(user.getUsername());
                    logLogin.setCreateTime(LocalDateTime.now());
                    publisher.publishEvent(new SysLoginLogEvent(logLogin));
                    String name = StringUtils.hasText(user.getRealName()) ? user.getRealName() : user.getUsername();
                    log.info("用户【{}】退出登录", name);
                    redisClient.delete(user.getSessionCacheKey());
                });
        return true;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
