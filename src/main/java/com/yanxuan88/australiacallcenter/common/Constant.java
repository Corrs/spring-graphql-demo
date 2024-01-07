package com.yanxuan88.australiacallcenter.common;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.ZoneId.SHORT_IDS;

public interface Constant {
    List<String> BLACK_USER_LIST = Collections.unmodifiableList(Arrays.asList("admin", "superadmin", "root", "administrator", "Administrator"));
    String DEFAULT_PASSWORD_MD5 = "14e1b600b1fd579f47433b88e8d85291";
    long SUPER_ADMIN_USER_ID = 1L;
    String HEADER_TOKEN_KEY = "Authentication";
    String TOKEN_UUID = "uuid";
    String IP = "ip";
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
