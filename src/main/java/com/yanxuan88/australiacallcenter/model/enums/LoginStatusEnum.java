package com.yanxuan88.australiacallcenter.model.enums;

import com.yanxuan88.australiacallcenter.common.IDict;

public enum LoginStatusEnum implements IDict<Integer> {
    /**
     * 失败
     */
    FAIL(0, "失败"),
    /**
     * 成功
     */
    SUCCESS(1, "成功"),
    /**
     * 账号已锁定
     */
    LOCK(2, "账号已锁定"),
    ;

    LoginStatusEnum(int code, String text) {
        init(code, text);
    }
}
