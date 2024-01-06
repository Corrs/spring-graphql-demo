package com.yanxuan88.australiacallcenter.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ming
 * @date 2020/9/10 11:16
 */
@Slf4j
public class IPUtil {
    private static final String LOCALHOST_V4 = "127.0.0.1";
    private static final String LOCALHOST_V6 = "0:0:0:0:0:0:0:1";
    private static final String UNKNOWN = "unknown";

    public static String getIpAddress(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        String ipAddress;
        try {
            ipAddress = request.getHeader("X-Forwarded-For");
            builder.append("\nX-Forwarded-For:").append(ipAddress).append(".");
            if (isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("X-Real-IP");
                builder.append("\nX-Real-IP:").append(ipAddress).append(".");
            }
            if (isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
                builder.append("\nProxy-Client-IP:").append(ipAddress).append(".");
            }
            if (isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
                builder.append("\nWL-Proxy-Client-IP:").append(ipAddress).append(".");
            }
            if (isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Http-Client-IP");
                builder.append("\nHttp-Client-IP:").append(ipAddress).append(".");
            }
            if (isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals(LOCALHOST_V4) || ipAddress.equals(LOCALHOST_V6)) {
                    ipAddress = LOCALHOST_V4;
                }
                builder.append("\nrequest.getRemoteAddr():").append(ipAddress).append(".");
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (isNotBlank(ipAddress) && ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        log.info(builder.toString());
        return ipAddress;

    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().length() <= 0;
    }

    private static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
