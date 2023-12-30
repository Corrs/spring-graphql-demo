package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.model.vo.LoginCaptchaVO;
import com.yanxuan88.australiacallcenter.model.vo.UserLoginInfoVO;
import com.yanxuan88.australiacallcenter.service.ILoginService;
import com.yanxuan88.australiacallcenter.util.RequestAttrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import static com.yanxuan88.australiacallcenter.common.Constant.HEADER_CAPTCHA_KEY;

/**
 * 用户登录控制器
 *
 * @author co
 * @since 2023-12-26 16:49:01
 */
@Controller
public class LoginController {

    @Autowired
    private ILoginService loginService;

    /**
     * 获取登录验证码
     *
     * @return LoginCaptchaVO
     */
    @QueryMapping
    public LoginCaptchaVO loginCaptcha() {
        return loginService.createLoginCaptcha((String) RequestAttrUtil.getAttribute(HEADER_CAPTCHA_KEY));
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     * @return UserLoginInfoVO
     */
    @MutationMapping
    public UserLoginInfoVO login(@Argument String username, @Argument String password, @Argument String captcha) {
        return loginService.login(username, password, captcha, (String) RequestAttrUtil.getAttribute(HEADER_CAPTCHA_KEY));
    }
}
