package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditDictTypeDTO {
    @Min(value = 1L, message = "无效的字典标识")
    @NotNull(message = "字典标识不能为空")
    private Long id;
    @Length(max = 255, message = "字典名称长度应在255字以内")
    @NotBlank(message = "字典名称不能为空")
    private String dictName;
    @Length(max = 100, message = "字典类型长度应在100字以内")
    @NotBlank(message = "字典类型不能为空")
    private String dictType;
    @Min(value = 0, message = "排序不能为负数")
    private Integer sort;
    @Length(max = 255, message = "备注长度应在255字以内")
    private String remark;
}
