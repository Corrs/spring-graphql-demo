package com.yanxuan88.australiacallcenter.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
public class AddUserDTO {
    @Length(max = 50, message = "真实姓名长度应在50字以内")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    @Pattern(regexp = "^[a-zA-Z][1-9a-zA-Z]+$", message = "用户名格式不正确")
    @Length(min = 8, max = 50, message = "用户名长度应在8-50字以内")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @Email(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确")
    @Length(max = 50, message = "邮箱长度应在50字以内")
    private String email;
    @Pattern(regexp = "^[0,1]\\d{10,14}$", message = "手机号格式不正确")
    @Length(min = 11, max = 15, message = "手机号应在11-15字以内")
    private String mobile;
    @Min(value = 1, message = "部门不存在")
    @NotNull(message = "部门不能为空")
    private Long deptId;
    @Range(max = 2, message = "性别不正确")
    private Integer gender;
    @Min(value = 1, message = "角色不存在")
    private Long roleId;
}
