package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditDeptDTO {
    @Min(value = 1L, message = "机构不存在")
    @NotNull(message = "机构标识不能为空")
    private Long id;
    @Min(value = 0L, message = "上级机构不存在")
    private Long pid;
    @NotBlank(message = "名称为空")
    @Length(max = 50, message = "名称应为1-50字")
    private String name;
    @Min(value = 0L, message = "排序应≥0")
    private Integer sort;

    public Long getPid() {
        return pid == null ? 0 : pid;
    }

    public Integer getSort() {
        return sort == null ? 0 : sort;
    }
}
