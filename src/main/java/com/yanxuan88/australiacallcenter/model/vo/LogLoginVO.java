package com.yanxuan88.australiacallcenter.model.vo;

import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.model.entity.SysLogLogin;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 登录日志VO类
 *
 * @author co
 * @since 2024-01-02 15:23:07
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class LogLoginVO {
    private Long id;
    private String username;
    private Integer operation;
    private Integer status;
    private String userAgent;
    private String ip;
    private LocalDateTime createTime;

    public LogLoginVO(SysLogLogin e) {
        if (e == null) throw new BizException("登录日志参数异常");
        id = e.getId();
        username = e.getCreateName();
        operation = e.getOperation();
        status = e.getStatus();
        userAgent = e.getUserAgent();
        ip = e.getIp();
        createTime = e.getCreateTime();
    }
}
