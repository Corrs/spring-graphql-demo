package com.yanxuan88.australiacallcenter.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DynamicJobVO {
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
