package com.yanxuan88.australiacallcenter.desensitize;

/**
 * 手机号脱敏
 *
 * @author co
 * @since 2024-01-09 16:16:30
 */
public class DesensitizationPassword implements Desensitization<String> {
    @Override
    public String desensitize(String source) {
        return DesensitizeUtil.desensitize(source);
    }
}
