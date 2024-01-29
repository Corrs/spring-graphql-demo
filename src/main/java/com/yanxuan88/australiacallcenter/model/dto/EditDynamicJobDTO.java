package com.yanxuan88.australiacallcenter.model.dto;

import com.yanxuan88.australiacallcenter.common.IDict;
import com.yanxuan88.australiacallcenter.scheduler.TriggerTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class EditDynamicJobDTO {
    @NotNull(message = "任务标识不能为空")
    @Min(value = 1, message = "无效的任务")
    private Long id;
    @Length(max = 250, message = "任务描述长度应在250字以内")
    private String description;
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
