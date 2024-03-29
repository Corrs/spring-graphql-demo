package com.yanxuan88.australiacallcenter.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.redis.channel.SubscribableRedisChannel;
import org.springframework.integration.redis.store.RedisChannelMessageStore;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.messaging.MessageChannel;

@Configuration
@AutoConfigureAfter(RedisConfiguration.class)
public class RedisSIConfiguration {
    /**
     * redis 连接工厂
     */
    private final RedisConnectionFactory connectionFactory;
    private final GenericJackson2JsonRedisSerializer redisValueSerializer;

    public RedisSIConfiguration(RedisConnectionFactory connectionFactory, GenericJackson2JsonRedisSerializer redisValueSerializer) {
        this.connectionFactory = connectionFactory;
        this.redisValueSerializer = redisValueSerializer;
    }

    @Bean
    public RedisChannelMessageStore redisChannelMessageStore() {
        RedisChannelMessageStore store = new RedisChannelMessageStore(connectionFactory);
        store.setValueSerializer(redisValueSerializer);
        return store;
    }

    @Bean
    public SubscribableRedisChannel noticeSubscribeChannel() {
        return new SubscribableRedisChannel(connectionFactory, "notice.topic");
    }

    @Bean
    public MessageChannel noticeChannel(RedisChannelMessageStore redisChannelMessageStore) {
        return new QueueChannel(new MessageGroupQueue(redisChannelMessageStore, "noticeChannel"));
    }

    @Bean
    public MessageChannel logChannel(RedisChannelMessageStore redisChannelMessageStore) {
        return new QueueChannel(new MessageGroupQueue(redisChannelMessageStore, "logChannel"));
    }

    @Bean
    public MessageChannel loginLogChannel(RedisChannelMessageStore redisChannelMessageStore) {
        return new QueueChannel(new MessageGroupQueue(redisChannelMessageStore, "loginLogChannel"));
    }

    @Bean
    public MessageChannel sysLogChannel(RedisChannelMessageStore redisChannelMessageStore) {
        return new QueueChannel(new MessageGroupQueue(redisChannelMessageStore, "sysLogChannel"));
    }

    @Bean
    public MessageChannel logoutChannel(RedisChannelMessageStore redisChannelMessageStore) {
        return new QueueChannel(new MessageGroupQueue(redisChannelMessageStore, "logoutChannel"));
    }

//    @Bean
//    public RedisLockRegistry redisLockRegistry() {
//        RedisLockRegistry lockRegistry = new RedisLockRegistry(connectionFactory, "lock", 30000L);
//        lockRegistry.setRedisLockType(RedisLockRegistry.RedisLockType.PUB_SUB_LOCK);
//        return lockRegistry;
//    }
}
