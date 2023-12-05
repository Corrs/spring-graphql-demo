package com.yanxuan88.australiacallcenter.common;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneId.SHORT_IDS;

public interface Constant {
    String HEADER_TOKEN_KEY = "Authentication";
    String TOKEN_PAYLOAD_KEY = "uuid";
    ZoneId DEFAULT_ZONE_ID = ZoneId.of("CTT", SHORT_IDS);
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(DEFAULT_ZONE_ID);
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(DEFAULT_ZONE_ID);
}
