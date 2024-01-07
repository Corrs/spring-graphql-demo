package com.yanxuan88.australiacallcenter.event.listener;

import com.yanxuan88.australiacallcenter.event.model.SysLogEvent;
import com.yanxuan88.australiacallcenter.event.model.SysLoginLogEvent;
import com.yanxuan88.australiacallcenter.service.ILogLoginService;
import com.yanxuan88.australiacallcenter.service.ILogOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SysLogListener {
    @Autowired
    private ILogLoginService logLoginService;
    @Autowired
    private ILogOperationService logOperationService;

    @Async
    @EventListener(SysLoginLogEvent.class)
    public void recordLoginLog(SysLoginLogEvent event) {
        if (event == null) return;
        logLoginService.save(event.getLoginLog());
    }

    @Async
    @EventListener(SysLogEvent.class)
    public void recordLog(SysLogEvent event) {
        if (event == null) return;
        logOperationService.save(event.getOperationLog());
    }
}
