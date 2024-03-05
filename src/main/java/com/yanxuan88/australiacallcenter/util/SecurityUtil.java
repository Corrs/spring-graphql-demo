package com.yanxuan88.australiacallcenter.util;

import com.google.common.hash.Hashing;
import com.yanxuan88.australiacallcenter.common.UserLoginInfo;
import com.yanxuan88.australiacallcenter.graphql.AusAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static com.yanxuan88.australiacallcenter.common.Constant.SUPER_ADMIN_USER_ID;

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

    public static String salt(String key) {
        if (key == null || key.trim().length() <= 0) {
            key = UUID.randomUUID().toString();
        }
        String salt = Hashing.sipHash24().newHasher().putString(key, StandardCharsets.UTF_8).hash().toString();
        return salt.length() > 20 ? salt.substring(0, 20) : salt;
    }

    public static String pwd(String salt, String pwd) {
        return Hashing.md5().newHasher().putString(salt + pwd, StandardCharsets.UTF_8).hash().toString();
    }

    public static boolean isSuperAdmin() {
        return isSuperAdmin(Optional.ofNullable(getUserLoginInfo()).map(UserLoginInfo::getUserId).orElse(null));
    }

    public static boolean isSuperAdmin(Long userId) {
        return userId == SUPER_ADMIN_USER_ID;
    }
}
