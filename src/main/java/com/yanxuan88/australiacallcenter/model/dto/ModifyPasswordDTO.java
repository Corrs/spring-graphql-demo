package com.yanxuan88.australiacallcenter.model.dto;

import com.yanxuan88.australiacallcenter.desensitize.DesensitizePassword;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class ModifyPasswordDTO {
    @DesensitizePassword
    @Length(max = 100, message = "密码长度超出限制")
    @NotBlank(message = "密码不能为空")
    private String password;
    @DesensitizePassword
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;
}
