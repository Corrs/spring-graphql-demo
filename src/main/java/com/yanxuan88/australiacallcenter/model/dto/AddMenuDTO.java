package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddMenuDTO {
    @NotNull(message = "类型不能为空")
    @Range(min = 1, max = 2, message = "无效的类型")
    private Integer type;
    @NotNull(message = "上级菜单不能为空")
    @Min(value = 0, message = "无效的上级菜单")
    private Long parentId;
    @NotBlank(message = "名称不能为空")
    @Length(max = 50, message = "名称长度应在50字以内")
    private String name;
    @Length(max = 200, message = "路由长度应在200字以内")
    private String url;
    @Min(value = 0, message = "排序不能<0")
    private Integer sort;
    @Length(max = 500, message = "授权标识长度应在500字以内")
    private String perms;
    @Length(max = 50, message = "图标长度应在50字以内")
    private String icon;

    public Integer getSort() {
        return sort == null ? 0 : sort;
    }
}
