package com.yanxuan88.australiacallcenter.model.enums;

import com.yanxuan88.australiacallcenter.common.IDict;

public enum UserStatusEnum implements IDict<Integer> {
    DISABLE(0, "禁用"),
    ENABLED(1, "正常"),
    ;

    UserStatusEnum(int code, String text) {
        init(code, text);
    }
}
