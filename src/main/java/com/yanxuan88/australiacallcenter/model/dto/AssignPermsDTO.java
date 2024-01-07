package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AssignPermsDTO {
    @Min(value = 1, message = "角色不存在")
    @NotNull(message = "角色标识不能为空")
    private Long roleId;
    private List<Long> perms;
}
