package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddDictDataDTO {
    @Min(value = 1L, message = "无效的字典类型标识")
    @NotNull(message = "字典类型标识不能为空")
    private Long dictTypeId;
    @NotBlank(message = "字典标签不能为空")
    @Length(max = 200, message = "字典标签长度应在200字以内")
    private String dictLabel;
    @NotBlank(message = "字典值不能为空")
    @Length(max = 200, message = "字典值长度应在200字以内")
    private String dictValue;
    @Min(value = 0, message = "排序不能<0")
    private Integer sort;
    @Length(max = 200, message = "备注长度应在200字以内")
    private String remark;
}
