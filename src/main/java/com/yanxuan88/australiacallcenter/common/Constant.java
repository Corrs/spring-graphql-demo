package com.yanxuan88.australiacallcenter.common;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static java.time.ZoneId.SHORT_IDS;

public interface Constant {
    String HEADER_TOKEN_KEY = "Authentication";
    String TOKEN_UUID = "uuid";
    String SESSION_KEY = "cache:session:";
    Long SESSION_EXPIRE = 2L;
    TimeUnit SESSION_EXPIRE_UNIT = TimeUnit.HOURS;
    String TOKEN_PAYLOAD_KEY = "uuid";
    String HEADER_CAPTCHA_KEY = "captchaKey";
    ZoneId DEFAULT_ZONE_ID = ZoneId.of("CTT", SHORT_IDS);
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(DEFAULT_ZONE_ID);
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(DEFAULT_ZONE_ID);
    /**
     * 逗号分割正则表达式
     */
    String COMMA_SPLIT_REG = "\\s*,\\s*";
}
