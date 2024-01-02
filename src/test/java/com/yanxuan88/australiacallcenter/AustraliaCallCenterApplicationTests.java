package com.yanxuan88.australiacallcenter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.time.LocalDateTime;

@SpringBootTest
class AustraliaCallCenterApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

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

}
