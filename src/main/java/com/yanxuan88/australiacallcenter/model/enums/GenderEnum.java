package com.yanxuan88.australiacallcenter.model.enums;

import com.yanxuan88.australiacallcenter.common.IDict;

public enum GenderEnum implements IDict<Integer> {
    GIRL(0, "女"),
    BOY(1, "男"),
    KNOWN(2, "保密")
    ;

    GenderEnum(int code, String text) {
        init(code, text);
    }
}
