package com.yanxuan88.australiacallcenter.model.enums;

import com.yanxuan88.australiacallcenter.common.IDict;

public enum LoginOperationEnum implements IDict<Integer> {
    LOGIN(0, "登录"),
    LOGOUT(1, "退出登录"),
    ;

    LoginOperationEnum(int code, String text) {
        init(code, text);
    }
}
