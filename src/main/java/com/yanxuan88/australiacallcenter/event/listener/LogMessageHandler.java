package com.yanxuan88.australiacallcenter.event.listener;

import com.yanxuan88.australiacallcenter.config.RedisClient;
import com.yanxuan88.australiacallcenter.event.model.LogoutEvent;
import com.yanxuan88.australiacallcenter.model.entity.SysLogLogin;
import com.yanxuan88.australiacallcenter.model.entity.SysLogOperation;
import com.yanxuan88.australiacallcenter.service.ILogLoginService;
import com.yanxuan88.australiacallcenter.service.ILogOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.EndpointId;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class LogMessageHandler {
    @Autowired
    private ILogLoginService logLoginService;
    @Autowired
    private ILogOperationService logOperationService;
    @Autowired
    private RedisClient redisClient;
    @EndpointId("loginLogHandler")
    @ServiceActivator(inputChannel = "loginLogChannel")
    @Async
    public void recordLoginLog(SysLogLogin obj) {
        if (obj == null) return;
        logLoginService.save(obj);
    }

    @EndpointId("sysLogHandler")
    @ServiceActivator(inputChannel = "sysLogChannel")
    @Async
    public void recordSysLog(SysLogOperation obj) {
        if (obj == null) return;
        logOperationService.save(obj);
    }

    @EndpointId("logoutClearSessionHandler")
    @ServiceActivator(inputChannel = "logoutChannel")
    @Async
    public void clearSession(Object obj) {
        if (obj == null || !StringUtils.hasText(((LogoutEvent)obj).getSessionCacheKey())) return;
        String sessionCacheKey = ((LogoutEvent)obj).getSessionCacheKey();
        log.info("clear session cache, key {}", sessionCacheKey);
        redisClient.delete(sessionCacheKey);
    }
}
