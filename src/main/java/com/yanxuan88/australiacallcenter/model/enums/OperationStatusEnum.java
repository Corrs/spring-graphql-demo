package com.yanxuan88.australiacallcenter.model.enums;

import com.yanxuan88.australiacallcenter.common.IDict;

public enum OperationStatusEnum implements IDict<Integer> {
    /**
     * 失败
     */
    FAIL(0, "失败"),
    /**
     * 成功
     */
    SUCCESS(1, "成功"),
    ;

    OperationStatusEnum(int code, String text) {
        init(code, text);
    }
}
