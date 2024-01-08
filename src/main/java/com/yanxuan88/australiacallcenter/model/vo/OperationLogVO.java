package com.yanxuan88.australiacallcenter.model.vo;

import com.yanxuan88.australiacallcenter.model.entity.SysLogOperation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OperationLogVO {
    private Long id;
    private String username;
    private LocalDateTime createTime;
    private String ip;
    private String userAgent;
    private String requestParams;
    private Long requestTime;
    private Integer status;
    private String operation;

    public OperationLogVO(SysLogOperation slo) {
        createTime = slo.getCreateTime();
        operation = slo.getOperation();
        username = slo.getCreateName();
        requestParams = slo.getRequestParams();
        requestTime = slo.getRequestTime();
        status = slo.getStatus();
        ip = slo.getIp();
        userAgent = slo.getUserAgent();
        id = slo.getId();
    }
}
