package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 */
@Data
@TableName("`sys_log_login`")
public class SysLogLogin {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 创建人（id）
     */
    private Long createUser;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    private String createName;
    private Integer operation;
    private Integer status;
    private String userAgent;
    private String ip;
}
