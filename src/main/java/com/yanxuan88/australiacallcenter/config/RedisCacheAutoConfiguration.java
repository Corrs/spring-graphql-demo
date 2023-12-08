package com.yanxuan88.australiacallcenter.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yanxuan88.australiacallcenter.common.Constant.DATE_FORMATTER;
import static com.yanxuan88.australiacallcenter.common.Constant.DATE_TIME_FORMATTER;

/**
 * Redis & Cache相关配置类
 * 配置了自定义KeyGenerator，因此必须继承CachingConfigurerSupport，否则不生效
 *
 * @author co
 * @since 2023-12-05 10:19:48
 */
@EnableCaching
@Configuration
@Import({RedisClient.class})
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {
    /**
     * redis 连接工厂
     */
    private final RedisConnectionFactory connectionFactory;

    /**
     * 缓存配置数据
     */
    private final CacheProperties cacheProperties;

    public RedisCacheAutoConfiguration(RedisConnectionFactory connectionFactory, CacheProperties cacheProperties) {
        this.connectionFactory = connectionFactory;
        this.cacheProperties = cacheProperties;
    }

    /**
     * 实例化RedisTemplate，使用Json序列化，并对日期/时间进行格式化，统一使用字符串key
     *
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        ObjectMapper objectMapper = new ObjectMapper();
        // 日期格式支持
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER)).addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER)).addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER)).addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        objectMapper.registerModule(timeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        // key统一使用字符串
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // value统一使用json
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        CacheProperties.Redis redisCacheProperties = cacheProperties.getRedis();
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(connectionFactory).cacheDefaults(redisCacheConfiguration(redisCacheProperties)).transactionAware().cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory, BatchStrategies.scan(1000)));
        if (redisCacheProperties.isEnableStatistics()) {
            builder.enableStatistics();
        }
        return builder.build();
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
     * @param redisCacheProperties redis缓存配置
     * @return RedisCacheConfiguration
     */
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties.Redis redisCacheProperties) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RedisCacheValueSerializer()));
        if (redisCacheProperties.getTimeToLive() != null) {
            cacheConfig = cacheConfig.entryTtl(redisCacheProperties.getTimeToLive());
        }
        if (redisCacheProperties.getKeyPrefix() != null) {
            cacheConfig = cacheConfig.prefixCacheNameWith(redisCacheProperties.getKeyPrefix());
        }
        if (!redisCacheProperties.isCacheNullValues()) {
            cacheConfig = cacheConfig.disableCachingNullValues();
        }
        if (!redisCacheProperties.isUseKeyPrefix()) {
            cacheConfig = cacheConfig.disableKeyPrefix();
        }
        // 这里会影响最终生成的缓存key
        if (StringUtils.hasText(redisCacheProperties.getKeyPrefix())) {
            cacheConfig = cacheConfig.prefixCacheNameWith(redisCacheProperties.getKeyPrefix());
        }
        return cacheConfig;
    }
}
