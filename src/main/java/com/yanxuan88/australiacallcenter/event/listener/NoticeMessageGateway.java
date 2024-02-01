package com.yanxuan88.australiacallcenter.event.listener;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "noticeMessageGateway", defaultRequestChannel = "noticeChannel")
public interface NoticeMessageGateway {
    void send(NoticeMessage message);
}
