package com.yanxuan88.australiacallcenter.model.enums;

import com.yanxuan88.australiacallcenter.common.IDict;

/**
 * Quartz trigger type
 *
 * @author caosh
 * @since 2024-01-26 21:00:00
 */
public enum TriggerTypeEnum implements IDict<Integer> {
    CRON(0, "cron"),
    SIMPLE(1, "simple"),
    CALENDAR(2, "calendar"),
    DAILY_TIME(3, "dailytime")
    ;

    TriggerTypeEnum(int code, String text) {
        init(code, text);
    }
}
