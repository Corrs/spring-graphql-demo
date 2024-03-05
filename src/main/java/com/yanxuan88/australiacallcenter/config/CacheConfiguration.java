package com.yanxuan88.australiacallcenter.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yanxuan88.australiacallcenter.common.Constant.DATE_FORMATTER;
import static com.yanxuan88.australiacallcenter.common.Constant.DATE_TIME_FORMATTER;

/**
 * 缓存配置，实现多级缓存
 * 配置了自定义KeyGenerator，因此必须继承CachingConfigurerSupport，否则不生效
 */
@EnableCaching
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration extends CachingConfigurerSupport {

    private static final String HASH = "#";
    /**
     * redis 连接工厂
     */
    private final RedisConnectionFactory connectionFactory;

    /**
     * 缓存配置数据
     */
    private final CacheProperties cacheProperties;

    public CacheConfiguration(RedisConnectionFactory connectionFactory, CacheProperties cacheProperties) {
        this.connectionFactory = connectionFactory;
        this.cacheProperties = cacheProperties;
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        // 使用spring提供的RedisCacheManager，不能对缓存设置不同的ttl
//        CacheProperties.Redis redisCacheProperties = cacheProperties.getRedis();
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(redisCacheConfiguration(redisCacheProperties))
//                .transactionAware()
//                .cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory, BatchStrategies.scan(1000)));
//        if (redisCacheProperties.isEnableStatistics()) {
//            builder.enableStatistics();
//        }
//        return customizerInvoker.customize(builder.build());
        // 使用自定义的RedisTtlCacheManager，对@Cacheable等注解做了增强
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory, BatchStrategies.scan(1000));
        RedisTtlCacheManager redisCacheManager = new RedisTtlCacheManager(cacheWriter, redisCacheConfiguration());
        redisCacheManager.setTransactionAware(true);
        return redisCacheManager;
    }

    /**
     * 自定义KeyGenerator
     * key结构 key-prefix+cacheName::类名.方法名(参数)
     * 最终生成的key会受到key-prefix和@Cacheable注解中配置的cacheNames影响 示例：
     * <b>@Cacheable(cacheNames = {"query.hello"})</b>
     * query.hello::HelloController.hello(spring)
     * 如果配置文件中配置了key-prefix: 'cache:'
     * cache:query.hello::HelloController.hello(world)
     *
     * @return KeyGenerator
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            String paramStr = Stream.of(params).map(Object::toString).collect(Collectors.joining(",", "(", ")"));
            return target.getClass().getSimpleName() + "." + method.getName() + paramStr;
        };
    }

    /**
     * redis cache configuration 配置
     * 如果需要缓存LocalDateTime等类型，需要在pom.xml中引入以下依赖，可根据错误提示进行配置
     * <dependency>
     * <groupId>com.fasterxml.jackson.datatype</groupId>
     * <artifactId>jackson-datatype-jsr310</artifactId>
     * </dependency>
     *
     * @return RedisCacheConfiguration
     */
    RedisCacheConfiguration redisCacheConfiguration() {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RedisCacheValueSerializer()));
        CacheProperties.Redis redis = cacheProperties.getRedis();
        if (redis.getTimeToLive() != null) {
            cacheConfig = cacheConfig.entryTtl(redis.getTimeToLive());
        }
        if (!redis.isCacheNullValues()) {
            cacheConfig = cacheConfig.disableCachingNullValues();
        }
        if (!redis.isUseKeyPrefix()) {
            cacheConfig = cacheConfig.disableKeyPrefix();
        }
        // 这里会影响最终生成的缓存key
        if (StringUtils.hasText(redis.getKeyPrefix())) {
            cacheConfig = cacheConfig.prefixCacheNameWith(redis.getKeyPrefix());
        }
        return cacheConfig;
    }

    class RedisTtlCacheManager extends RedisCacheManager {

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
            if (cacheConfig != null) {
                // 转换时间，支持时间单位例如：300ms，第二个参数是默认单位
                Duration duration = DurationStyle.detectAndParse(cacheArray[cacheArray.length - 1], ChronoUnit.SECONDS);
                cacheConfig = cacheConfig.entryTtl(duration);
            }
            return super.createRedisCache(name, cacheConfig);
        }
    }

    class RedisCacheValueSerializer extends GenericJackson2JsonRedisSerializer {
        public RedisCacheValueSerializer() {
            super();
        }

        @Override
        public Object deserialize(byte[] source) throws SerializationException {
            Object value = super.deserialize(source);
            if (value instanceof String) {
                String str = value == null ? "" : (String) value;
                // 以下为LocalDate和LocalDateTime的判断和转换，可以替换为其他更具效率的方式
                try {
                    if (str.length() == 10) {
                        return LocalDate.parse(str, DATE_FORMATTER);
                    }
                    if (str.length() == 19) {
                        return LocalDateTime.parse(str, DATE_TIME_FORMATTER);
                    }
                } catch (DateTimeParseException ex) {

                }
            }
            return value;
        }

        @Override
        public byte[] serialize(Object source) throws SerializationException {
            if (source instanceof LocalDateTime) {
                return super.serialize(((LocalDateTime) source).format(DATE_TIME_FORMATTER));
            }

            if (source instanceof LocalDate) {
                return super.serialize(((LocalDate) source).format(DATE_FORMATTER));
            }
            return super.serialize(source);
        }
    }
}
