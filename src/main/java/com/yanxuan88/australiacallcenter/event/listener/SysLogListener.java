package com.yanxuan88.australiacallcenter.event.listener;

import com.yanxuan88.australiacallcenter.event.model.SysLoginLogEvent;
import com.yanxuan88.australiacallcenter.model.entity.SysLogLogin;
import com.yanxuan88.australiacallcenter.service.ILogLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SysLogListener {
    @Autowired
    private ILogLoginService logLoginService;

    @Async
    @EventListener(SysLoginLogEvent.class)
    public void recordLoginLog(SysLoginLogEvent event) {
        log.info("记录登录/退出登录日志");
        if (event == null) return;
        SysLogLogin loginLog = event.getLoginLog();
        log.info("日志内容：{}", loginLog);
        boolean saveResult = logLoginService.save(loginLog);
        log.info("保存结果：{}", saveResult ? "成功" : "失败");
    }
}
