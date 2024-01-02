package com.yanxuan88.australiacallcenter.service.impl;

import com.wf.captcha.SpecCaptcha;
import com.yanxuan88.australiacallcenter.config.RedisClient;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;
import com.yanxuan88.australiacallcenter.model.vo.LoginCaptchaVO;
import com.yanxuan88.australiacallcenter.model.vo.UserBaseVO;
import com.yanxuan88.australiacallcenter.model.vo.UserLoginInfoVO;
import com.yanxuan88.australiacallcenter.service.ILoginService;
import com.yanxuan88.australiacallcenter.service.IUserService;
import com.yanxuan88.australiacallcenter.util.JWTUtil;
import com.yanxuan88.australiacallcenter.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.yanxuan88.australiacallcenter.common.Constant.*;
import static com.yanxuan88.australiacallcenter.exception.BaseResultCodeEnum.*;

@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {
    private static final String CACHE_LOGIN_CAPTCHA_PREFIX = "cache:loginCaptcha:";
    @Autowired
    private RedisClient redisClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;

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
     * 4. 缓存用户登录信息
     * 5. 删除验证码缓存，组装VO，返回给前端
     *
     * @param username   用户名
     * @param password   密码
     * @param captcha    验证码
     * @param captchaKey 验证码key
     * @return UserLoginInfoVO
     */
    @Override
    public UserLoginInfoVO login(String username, String password, String captcha, String captchaKey) {
        log.info("用户登录，username={}，password={}，captcha={}，captchaKey={}", username, password, captcha, captchaKey);
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

        // 2.
        SysUser user = userService.queryByUsername(username);
        if (user == null) {
            throw new BizException(USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(SecurityUtil.pwd(user.getSalt(), password), user.getPassword())) {
            throw new BizException(PASSWORD_FAIL);
        }

        // 3. todo

        // 4. todo

        // 5.
        redisClient.delete(captchaCacheKey);
        String uuid = UUID.randomUUID().toString();
        String authenticationToken = JWTUtil.generateToken(Collections.singletonMap(TOKEN_UUID, uuid));

        UserLoginInfoVO result = new UserLoginInfoVO()
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
                    String name = StringUtils.hasText(user.getRealName()) ? user.getRealName() : user.getUsername();
                    log.info("用户【{}】退出登录", name);
                    redisClient.delete(user.getSessionCacheKey());
                });
        return true;
    }
}
