package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class AddRoleDTO {
    @Length(max = 50, message = "角色名称长度应在50字以内")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    @Length(max = 100, message = "备注长度应在100字以内")
    private String remark;
}
