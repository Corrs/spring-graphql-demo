package com.yanxuan88.australiacallcenter.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Lists;
import com.yanxuan88.australiacallcenter.common.Constant;
import com.yanxuan88.australiacallcenter.exception.BaseResultCodeEnum;
import com.yanxuan88.australiacallcenter.exception.BizException;
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

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(Constant.HEADER_TOKEN_KEY);
        if (StringUtils.hasText(token)) {
            try {
                DecodedJWT decodedJWT = JWTUtil.decodedJWT(token);
                String uuid = decodedJWT.getClaim(Constant.TOKEN_PAYLOAD_KEY).asString();
                // todo 去redis中查数据，并生成AusAuthenticationToken
                AusAuthenticationToken authentication = AusAuthenticationToken.authenticated("", null, Lists.newArrayList(new AusGrantedAuthority("USER", Lists.newArrayList("hello"))));
                // 将数据放到SecurityContextHolder中
                SecurityContextHolder.getContext().setAuthentication(authentication);
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
