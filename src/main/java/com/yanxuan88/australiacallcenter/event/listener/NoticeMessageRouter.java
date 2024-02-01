package com.yanxuan88.australiacallcenter.event.listener;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;

@MessageEndpoint
public class NoticeMessageRouter {
    @Router(inputChannel = "noticeChannel")
    public String toChannel(NoticeMessage message) {
        return message.toChannel();
    }
}
