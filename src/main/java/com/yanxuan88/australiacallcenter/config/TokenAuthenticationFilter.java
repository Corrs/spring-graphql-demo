package com.yanxuan88.australiacallcenter.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Lists;
import com.yanxuan88.australiacallcenter.common.Constant;
import com.yanxuan88.australiacallcenter.common.UserLoginInfo;
import com.yanxuan88.australiacallcenter.model.vo.UserBaseVO;
import com.yanxuan88.australiacallcenter.model.vo.UserLoginInfoVO;
import com.yanxuan88.australiacallcenter.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.yanxuan88.australiacallcenter.common.Constant.*;
import static com.yanxuan88.australiacallcenter.common.Constant.SESSION_EXPIRE_UNIT;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final RedisClient redisClient;
    public TokenAuthenticationFilter(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.setAttribute(HEADER_CAPTCHA_KEY, request.getHeader(HEADER_CAPTCHA_KEY));
        String token = request.getHeader(Constant.HEADER_TOKEN_KEY);
        if (StringUtils.hasText(token)) {
            try {
                DecodedJWT decodedJWT = JWTUtil.decodedJWT(token);
                String uuid = decodedJWT.getClaim(Constant.TOKEN_PAYLOAD_KEY).asString();
                String sessionCacheKey = SESSION_KEY.concat(uuid);
                List<Object> objects = redisClient.piPipelined(operations -> {
                    redisClient.get(sessionCacheKey);
                    redisClient.expire(sessionCacheKey, SESSION_EXPIRE, SESSION_EXPIRE_UNIT);
                });
                UserLoginInfoVO user = (UserLoginInfoVO) objects.get(0);
                if (user != null && token.equals(user.getAuthenticationToken())) {
                    UserBaseVO userBase = user.getUser();
                    UserLoginInfo credentials = new UserLoginInfo();
                    credentials.setUsername(userBase.getUsername());
                    credentials.setUserId(userBase.getUserId());
                    credentials.setEmail(userBase.getEmail());
                    credentials.setMobile(userBase.getMobile());
                    AusAuthenticationToken authentication = AusAuthenticationToken.authenticated("", credentials, Lists.newArrayList(new AusGrantedAuthority("USER", Lists.newArrayList("hello"))));
                    // 将数据放到SecurityContextHolder中
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTVerificationException e) {
                log.error("token校验失败，异常类：{}", e.getClass().getSimpleName());
//                if (e instanceof TokenExpiredException) {
//                    throw new BizException(BaseResultCodeEnum.TOKEN_EXPIRE);
//                }
//                throw new BizException(BaseResultCodeEnum.TOKEN_FAIL);
            }
        }
        filterChain.doFilter(request, response);
    }
}
