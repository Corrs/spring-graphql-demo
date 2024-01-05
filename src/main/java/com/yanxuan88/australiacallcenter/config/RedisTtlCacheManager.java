package com.yanxuan88.australiacallcenter.config;

import org.springframework.boot.convert.DurationStyle;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class RedisTtlCacheManager extends RedisCacheManager {
    private static final String HASH = "#";

    public RedisTtlCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        if (!StringUtils.hasText(name) || !name.contains(HASH)) {
            return super.createRedisCache(name, cacheConfig);
        }
        String[] cacheArray = name.split(HASH);
        if (cacheArray.length < 2) {
            return super.createRedisCache(name, cacheConfig);
        }
        String cacheName = cacheArray[0];
        if (cacheConfig != null) {
            // 转换时间，支持时间单位例如：300ms，第二个参数是默认单位
            Duration duration = DurationStyle.detectAndParse(cacheArray[1], ChronoUnit.SECONDS);
            cacheConfig = cacheConfig.entryTtl(duration);
        }
        return super.createRedisCache(cacheName, cacheConfig);
    }
}
