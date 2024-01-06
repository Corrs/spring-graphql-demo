package com.yanxuan88.australiacallcenter.model.enums;

import com.yanxuan88.australiacallcenter.common.IDict;

public enum MenuTypeEnum implements IDict<Integer> {
    MENU(1, "菜单"),
    BTN(2, "按钮"),
    ;

    MenuTypeEnum(int code, String text) {
        init(code, text);
    }
}
