package com.yanxuan88.australiacallcenter.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public final class RequestAttrUtil {
    /**
     * 获取request attribute的值
     *
     * @param name  name
     * @param scope 0 request  1 session
     * @return Object or null
     */
    public static Object getAttribute(String name, int scope) {
        return getRequestAttributes().getAttribute(name, scope);
    }

    /**
     * 获取request attribute的值，scope为request
     *
     * @param name name
     * @return Object or null
     */
    public static Object getAttribute(String name) {
        return getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }


    public static RequestAttributes getRequestAttributes() {
        return RequestContextHolder.currentRequestAttributes();
    }
}
