package com.yanxuan88.australiacallcenter.util;

import com.yanxuan88.australiacallcenter.common.UserLoginInfo;
import com.yanxuan88.australiacallcenter.config.AusAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {
    /**
     * 获取AuthenticationToken
     *
     * @return AuthenticationToken
     */
    public static AusAuthenticationToken getAuthenticationToken() {
        SecurityContext context = getContext();
        return context != null ? (AusAuthenticationToken) context.getAuthentication() : null;
    }

    /**
     * 获取 SecurityContext
     *
     * @return SecurityContext
     */
    public static SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    /**
     * 获取登录用户信息
     *
     * @return UserLoginInfo
     */
    public static UserLoginInfo getUserLoginInfo() {
        AusAuthenticationToken token = getAuthenticationToken();
        return token != null ? (UserLoginInfo) token.getCredentials() : null;
    }
}
