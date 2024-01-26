package com.yanxuan88.australiacallcenter.event.listener;

import org.springframework.integration.annotation.EndpointId;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class TestEIP {
    @EndpointId("test")
    @ServiceActivator(inputChannel = "redisChannel")
    public void aa(String payload) {
        System.out.println(payload);
    }
}
