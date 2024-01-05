package com.yanxuan88.australiacallcenter.service;

import com.yanxuan88.australiacallcenter.model.vo.LoginCaptchaVO;
import com.yanxuan88.australiacallcenter.model.vo.UserLoginInfoVO;

public interface ILoginService {
    LoginCaptchaVO createLoginCaptcha(String loginCaptchaKey);

    UserLoginInfoVO login(String username, String password, String captcha);

    boolean logout();

}
