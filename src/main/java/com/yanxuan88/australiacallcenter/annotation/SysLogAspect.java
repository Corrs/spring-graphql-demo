package com.yanxuan88.australiacallcenter.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanxuan88.australiacallcenter.common.UserLoginInfo;
import com.yanxuan88.australiacallcenter.event.listener.LogMessageGateway;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.model.entity.SysLogOperation;
import com.yanxuan88.australiacallcenter.util.RequestAttrUtil;
import com.yanxuan88.australiacallcenter.util.SecurityUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.yanxuan88.australiacallcenter.common.Constant.IP;
import static com.yanxuan88.australiacallcenter.model.enums.OperationStatusEnum.FAIL;
import static com.yanxuan88.australiacallcenter.model.enums.OperationStatusEnum.SUCCESS;

@Slf4j
@Aspect
@Component
public class SysLogAspect {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private LogMessageGateway logMessageGateway;

    @Pointcut("@annotation(com.yanxuan88.australiacallcenter.annotation.SysLog)")
    public void sysLog() {
    }

    @SneakyThrows
    @Around("sysLog() && @annotation(sysLog)")
    public Object doSysLog(ProceedingJoinPoint pjp, SysLog sysLog) {
        Long operationRequestTime = null;
        Integer operationStatus = null;
        StopWatch watch = new StopWatch();
        watch.start();
        String requestParams = MAPPER.writeValueAsString(pjp.getArgs());
        try {
            Object retVal = pjp.proceed();
            watch.stop();
            operationRequestTime = watch.getLastTaskTimeMillis();
            operationStatus = SUCCESS.getCode();
            log.info("\n请求描述：{}\n请求参数：{}\n处理结果：{}\n处理时长：{}ms", sysLog.value(), requestParams, retVal, operationRequestTime);
            return retVal;
        } catch (Throwable e) {
            watch.stop();
            operationRequestTime = watch.getLastTaskTimeMillis();
            operationStatus = FAIL.getCode();
            log.error("\n请求描述：{}\n请求参数：{}\n异常信息：{}\n处理时长：{}ms", sysLog.value(), requestParams, e.getMessage(), operationRequestTime);
            throw new BizException(e);
        } finally {
            if (sysLog.saveToDB()) {
                String ip = (String) RequestAttrUtil.getAttribute(IP);
                String userAgent = (String) RequestAttrUtil.getAttribute(HttpHeaders.USER_AGENT);
                UserLoginInfo user = SecurityUtil.getUserLoginInfo();
                SysLogOperation logOperation = new SysLogOperation()
                        .setRequestTime(operationRequestTime)
                        .setRequestParams(requestParams)
                        .setOperation(sysLog.value())
                        .setIp(ip)
                        .setStatus(operationStatus)
                        .setUserAgent(userAgent)
                        .setCreateTime(LocalDateTime.now())
                        .setCreateName(Optional.ofNullable(user).map(UserLoginInfo::getUsername).orElse(""))
                        .setCreateUser(Optional.ofNullable(user).map(UserLoginInfo::getUserId).orElse(null));
                logMessageGateway.sendToLogger(logOperation);
            }
        }
    }

}
