package com.yanxuan88.australiacallcenter.desensitize;

import com.google.common.base.Strings;

/**
 * 脱敏实体类
 *
 * @author co
 * @since 2024-01-09 16:14:10
 */
public final class DesensitizeUtil {
    public static final String SYMBOL = "*";

    public static String mobile(String mobile) {
        return masking(mobile, 3, 4);
    }

    public static String email(String email) {
        if (isBlank(email)) return "";
        int idx = email.indexOf("@");
        if (idx < 1) return email;
        return email.substring(0, 1).concat(Strings.repeat(SYMBOL, idx - 1)).concat(email.substring(idx));
    }

    public static String masking(String str, int pre, int suf) {
        if (isBlank(str) || (pre <= 0 && suf <= 0)) return "";
        int len = str.length();
        if (len > (pre + suf)) {
            return str.substring(0, pre).concat(Strings.repeat(SYMBOL, len - pre - suf)).concat(str.substring(len - suf));
        } else if (len > pre && pre > 0) {
            return str.substring(0, pre).concat(Strings.repeat(SYMBOL, len - pre));
        } else if (len > suf && suf > 0) {
            return Strings.repeat(SYMBOL, len - suf).concat(str.substring(len - suf));
        }
        return Strings.repeat(SYMBOL, len);
    }

    private static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public static String masking(String value) {
        return isBlank(value) ? null : Strings.repeat(SYMBOL, value.length());
    }

    public static String desensitize(String str) {
        return isBlank(str) ? "" : Strings.repeat(SYMBOL, str.trim().length());
    }
}
