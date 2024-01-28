package com.yanxuan88.australiacallcenter.model.dto;

import com.yanxuan88.australiacallcenter.common.IDict;
import com.yanxuan88.australiacallcenter.model.enums.TriggerTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class AddDynamicJobDTO {
    @Length(max = 190, message = "任务名称长度应在190字以内")
    @NotBlank(message = "任务名称不能为空")
    private String jobName;
    @Length(max = 190, message = "任务分组长度应在190字以内")
    @NotBlank(message = "任务分组不能为空")
    private String jobGroup;
    @Length(max = 250, message = "任务描述长度应在250字以内")
    private String description;
    @Length(max = 250, message = "任务类路径长度应在190字以内")
    @NotBlank(message = "任务类路径不能为空")
    private String jobClassName;
    private String jobData;
    private Boolean status;
    private Integer triggerType;
    @NotBlank(message = "触发器规则不能为空")
    private String triggerRule;
    private LocalDateTime firstRuntime;

    public Boolean getStatus() {
        return Boolean.TRUE.equals(status);
    }

    public Integer getTriggerType() {
        TriggerTypeEnum type = IDict.getByCode(TriggerTypeEnum.class, triggerType);
        return type == null ? TriggerTypeEnum.CRON.getCode() : triggerType;
    }
}
