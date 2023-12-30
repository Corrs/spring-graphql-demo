package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class AddDeptDTO {
    private Long pid;
    @NotBlank(message = "部门名称为空")
    @Length(max = 50, message = "部门名称应为1-50字")
    private String name;
    private Integer sort;

    public Long getPid() {
        return pid == null ? 0 : pid;
    }

    public Integer getSort() {
        return sort == null ? 0 : sort;
    }
}
