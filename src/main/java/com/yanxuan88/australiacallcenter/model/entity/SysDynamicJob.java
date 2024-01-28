package com.yanxuan88.australiacallcenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 动态定时任务表
 *
 * @author caosh
 * @since 2024-01-26 20:24:38
 */
@Data
@TableName("`sys_dynamic_job`")
public class SysDynamicJob {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClassName;
    private String jobData;
    private Boolean status;
    private Integer triggerType;
    private String triggerRule;
    private LocalDateTime firstRuntime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
