package com.yanxuan88.australiacallcenter.model.dto;

import com.yanxuan88.australiacallcenter.common.Constant;
import com.yanxuan88.australiacallcenter.desensitize.DesensitizeEmail;
import com.yanxuan88.australiacallcenter.desensitize.DesensitizeMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
public class EditUserDTO {
    @Min(value = 1, message = "用户不存在")
    @NotNull(message = "用户标识不能为空")
    private Long id;
    @Length(max = 50, message = "真实姓名长度应在50字以内")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    @DesensitizeEmail
    @Email(regexp = Constant.EMAIL_REG, message = "邮箱格式不正确")
    @Length(max = 50, message = "邮箱长度应在50字以内")
    private String email;
    @DesensitizeMobile
    @Pattern(regexp = Constant.MOBILE_REG, message = "手机号格式不正确")
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
