package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class AddDictTypeDTO {
    @Length(max = 255, message = "字典名称长度应在255字以内")
    @NotBlank(message = "字典名称不能为空")
    private String dictName;
    @Length(max = 100, message = "字典类型长度应在100字以内")
    @NotBlank(message = "字典类型不能为空")
    private String dictType;
    @Min(value = 0, message = "排序不能<0")
    private Integer sort;
    @Length(max = 255, message = "备注长度应在255字以内")
    private String remark;
}
