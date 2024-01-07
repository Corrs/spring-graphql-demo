package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("`sys_log_operation`")
public class SysLogOperation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String operation;
    private String requestParams;
    private String userAgent;
    private Long requestTime;
    private String ip;
    private Integer status;
    private String createName;
    private Long createUser;
    private LocalDateTime createTime;
}
