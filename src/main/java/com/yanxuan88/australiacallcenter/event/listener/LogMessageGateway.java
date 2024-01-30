package com.yanxuan88.australiacallcenter.event.listener;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "logMessageGateway", defaultRequestChannel = "logChannel")
public interface LogMessageGateway {
    void sendToLogger(Object data);
}
