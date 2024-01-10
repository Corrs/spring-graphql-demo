package com.yanxuan88.australiacallcenter.event.listener;

import com.yanxuan88.australiacallcenter.config.RedisClient;
import com.yanxuan88.australiacallcenter.event.model.LogoutEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class LogoutListener {
    @Autowired
    private RedisClient redisClient;

    @Async
    @EventListener(LogoutEvent.class)
    public void clearSession(LogoutEvent event) {
        if (event == null || !StringUtils.hasText(event.getSessionCacheKey())) return;
        String sessionCacheKey = event.getSessionCacheKey();
        log.info("clear session cache, key {}", sessionCacheKey);
        redisClient.delete(sessionCacheKey);
    }
}
