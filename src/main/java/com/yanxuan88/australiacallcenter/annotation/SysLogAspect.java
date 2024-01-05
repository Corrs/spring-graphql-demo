package com.yanxuan88.australiacallcenter.annotation;

import com.yanxuan88.australiacallcenter.exception.BizException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SysLogAspect {
    @Pointcut("@annotation(com.yanxuan88.australiacallcenter.annotation.SysLog)")
    public void sysLog() {
    }

    @SneakyThrows
    @Around("sysLog() && @annotation(sysLog)")
    public Object doSysLog(ProceedingJoinPoint pjp, SysLog sysLog) {
        // start stopwatch
        Object retVal = null;
        try {
            retVal = pjp.proceed();
            log.info("\n请求描述：{}\n请求参数：{}\n处理结果：{}", sysLog.value(), pjp.getArgs(), retVal);
        } catch (Throwable e) {
            log.error("\n请求描述：{}\n请求参数：{}\n异常信息：{}", sysLog.value(), pjp.getArgs(), e.getMessage());
            throw new BizException(e);
        }
        // stop stopwatch
        return retVal;
    }
}
