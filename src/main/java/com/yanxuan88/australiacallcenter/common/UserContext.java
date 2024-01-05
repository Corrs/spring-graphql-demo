package com.yanxuan88.australiacallcenter.common;


import com.yanxuan88.australiacallcenter.util.SecurityUtil;

import java.util.Optional;

public class UserContext {
    public static UserLoginInfo get() {
        return SecurityUtil.getUserLoginInfo();
    }

    public static boolean isLogin() {
        return Optional.ofNullable(get()).isPresent();
    }

    public static Long getUserId() {
        return get().getUserId();
    }

    public static Long getUserIdOrNull() {
        return Optional.ofNullable(get()).map(UserLoginInfo::getUserId).orElse(null);
    }

    public static Long getUserIdOrDefault() {
        return Optional.ofNullable(get()).map(UserLoginInfo::getUserId).orElse(0L);
    }

    public static String getUsernameOrDefault() {
        return Optional.ofNullable(get())
                .map(user -> {
                    String realName = user.getRealName();
                    String username = user.getUsername();
                    return (realName == null || realName.trim().length() <= 0) ? username : realName;
                })
                .orElse("");
    }

    public static long getSysUserId() {
        return 1L;
    }
}
