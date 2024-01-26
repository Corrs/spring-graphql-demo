package com.yanxuan88.australiacallcenter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.redis.channel.SubscribableRedisChannel;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@SpringBootTest
class AustraliaCallCenterApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    SubscribableRedisChannel redisChannel;
    @Autowired
    RedisLockRegistry redisLockRegistry;

    @Test
    void test() {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Person p = new Person(1L, LocalDateTime.now());
        ops.set("aa", p);
        Person dPerson = (Person) ops.get("aa");
        System.out.println(dPerson);
    }
    @Test
    void contextLoads() {
    }

    @Test
    void testDefaultPwd() {
        System.out.println(passwordEncoder.encode("3eea4ff3a0217332d2eef34bc4bfab06"));
    }

    static class Person implements Serializable {
        private Long userId;
        private LocalDateTime birth;

        public Person() {
        }

        public Person(Long userId, LocalDateTime birth) {
            this.userId = userId;
            this.birth = birth;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public LocalDateTime getBirth() {
            return birth;
        }

        public void setBirth(LocalDateTime birth) {
            this.birth = birth;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "userId=" + userId +
                    ", birth=" + birth +
                    '}';
        }
    }

    @Test
    void testSendRedisMsg() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MessagingTemplate template = new MessagingTemplate();
        template.send(redisChannel, MessageBuilder.withPayload(mapper.writeValueAsString(Collections.singletonMap("k", "213123"))).build());
    }
    @Test
    void testRedisLockRegistry() throws InterruptedException {
        // 需要注意，这个redis分布式锁没有看门狗，不会自动续期
        Lock lock = redisLockRegistry.obtain("aaaaaa");
        lock.lock();
        TimeUnit.SECONDS.sleep(20L);
        lock.unlock();
    }

}
