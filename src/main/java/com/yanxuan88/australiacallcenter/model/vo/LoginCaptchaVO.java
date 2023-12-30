package com.yanxuan88.australiacallcenter.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录验证码vo
 *
 * @author co
 * @since 2023-12-26 16:47:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCaptchaVO {
    /**
     * 验证码图片(base64编码)
     */
    private String image;

    /**
     * 用户标识
     */
    private String key;
}
