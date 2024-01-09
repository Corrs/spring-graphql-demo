package com.yanxuan88.australiacallcenter.desensitize;

import com.yanxuan88.australiacallcenter.common.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号脱敏
 *
 * @author co
 * @since 2024-01-09 16:16:30
 */
public class DesensitizationEmail implements Desensitization<String> {
    Pattern pattern = Pattern.compile(Constant.EMAIL_REG);

    @Override
    public String desensitize(String source) {
        if (source == null) return null;
        Matcher matcher = pattern.matcher(source);
        if (matcher.matches()) {
            return DesensitizeUtil.email(matcher.group());
        }
        return null;
    }
}
