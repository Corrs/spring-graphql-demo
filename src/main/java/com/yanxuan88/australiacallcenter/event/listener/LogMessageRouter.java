package com.yanxuan88.australiacallcenter.event.listener;

import com.yanxuan88.australiacallcenter.model.entity.SysLogLogin;
import com.yanxuan88.australiacallcenter.model.entity.SysLogOperation;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;

@MessageEndpoint
public class LogMessageRouter {
    @Router(inputChannel = "logChannel")
    public String resolveLogChannel(Object obj) {
        return (obj instanceof SysLogLogin) ? "loginLogChannel" : ((obj instanceof SysLogOperation) ? "sysLogChannel" : "logoutChannel");
    }
}
