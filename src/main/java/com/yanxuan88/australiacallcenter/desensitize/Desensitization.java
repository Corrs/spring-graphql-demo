package com.yanxuan88.australiacallcenter.desensitize;

/**
 * 脱敏接口
 *
 * @author co
 * @since 2024-01-09 16:15:53
 */
public interface Desensitization<T> {
    T desensitize(String source);
}
