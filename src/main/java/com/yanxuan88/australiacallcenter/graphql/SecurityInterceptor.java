package com.yanxuan88.australiacallcenter.graphql;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yanxuan88.australiacallcenter.common.Constant;
import com.yanxuan88.australiacallcenter.common.UserLoginInfo;
import com.yanxuan88.australiacallcenter.graphql.AusAuthenticationToken;
import com.yanxuan88.australiacallcenter.graphql.AusGrantedAuthority;
import com.yanxuan88.australiacallcenter.redis.RedisClient;
import com.yanxuan88.australiacallcenter.model.vo.UserBaseVO;
import com.yanxuan88.australiacallcenter.model.vo.UserLoginInfoVO;
import com.yanxuan88.australiacallcenter.model.vo.UserPermissionVO;
import com.yanxuan88.australiacallcenter.util.IPUtil;
import com.yanxuan88.australiacallcenter.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yanxuan88.australiacallcenter.common.Constant.*;
import static com.yanxuan88.australiacallcenter.common.Constant.SESSION_EXPIRE_UNIT;

@Slf4j
@Component
public class SecurityInterceptor implements WebGraphQlInterceptor {
    private final RedisClient redisClient;

    public SecurityInterceptor(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        Map<String, Object> extensions = request.getExtensions();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        httpServletRequest.setAttribute(HEADER_CAPTCHA_KEY, extensions.get(HEADER_CAPTCHA_KEY));
        httpServletRequest.setAttribute(IP, IPUtil.getIpAddress(httpServletRequest));
        httpServletRequest.setAttribute(HttpHeaders.USER_AGENT, httpServletRequest.getHeader(HttpHeaders.USER_AGENT));
        String token = (String) extensions.get(Constant.HEADER_TOKEN_KEY);
        if (StringUtils.hasText(token)) {
            try {
                DecodedJWT decodedJWT = JWTUtil.decodedJWT(token);
                String uuid = decodedJWT.getClaim(Constant.TOKEN_PAYLOAD_KEY).asString();
                String sessionCacheKey = SESSION_KEY.concat(uuid);
                httpServletRequest.setAttribute(TOKEN_CACHE, sessionCacheKey);
                List<Object> objects = redisClient.piPipelined(operations -> {
                    redisClient.get(sessionCacheKey);
                    redisClient.expire(sessionCacheKey, SESSION_EXPIRE, SESSION_EXPIRE_UNIT);
                });
                UserLoginInfoVO user = (UserLoginInfoVO) objects.get(0);
                if (user != null) {
                    UserBaseVO userBase = user.getUser();
                    UserLoginInfo credentials = new UserLoginInfo();
                    credentials.setUsername(userBase.getUsername());
                    credentials.setUserId(userBase.getUserId());
                    credentials.setEmail(userBase.getEmail());
                    credentials.setMobile(userBase.getMobile());
                    credentials.setSessionCacheKey(sessionCacheKey);
                    credentials.setRealName(userBase.getRealName());
                    credentials.setAvatar(userBase.getAvatar());
                    credentials.setDeptId(userBase.getDeptId());
                    credentials.setSuperAdmin(userBase.getSuperAdmin());
                    List<String> permissions = Optional.ofNullable(user.getPermissions()).orElseGet(Collections::emptyList)
                            .stream()
                            .map(UserPermissionVO::getPerms)
                            .flatMap(e -> Stream.of(e.split(",")))
                            .filter(StringUtils::hasText)
                            .map(String::trim)
                            .collect(Collectors.toList());
                    AusGrantedAuthority authority = new AusGrantedAuthority(user.getRole(), permissions);
                    AusAuthenticationToken authentication = AusAuthenticationToken.authenticated("", credentials, authority);
                    // 将数据放到SecurityContextHolder中
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTVerificationException e) {
                log.error("token校验失败，异常类：{}", e.getClass().getSimpleName());
            }
        }
        return chain.next(request);
    }
}
