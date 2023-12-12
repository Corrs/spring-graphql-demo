package com.yanxuan88.australiacallcenter.graphql;

import org.springframework.graphql.execution.ThreadLocalAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

/**
 * 将Servlet容器线程传播到GraphQL
 * 通过GraphQlWebMvcAutoConfiguration注入到WebGraphQlHandler
 *
 * @author co
 * @since 2023-12-08 16:11:13
 */
@Component
public class RequestAttributesAccessor implements ThreadLocalAccessor {
    private static final String KEY = RequestAttributesAccessor.class.getName();

    @Override
    public void extractValues(Map<String, Object> container) {
        container.put(KEY, RequestContextHolder.getRequestAttributes());
    }

    @Override
    public void restoreValues(Map<String, Object> values) {
        if (values.containsKey(KEY)) {
            RequestContextHolder.setRequestAttributes((RequestAttributes) values.get(KEY));
        }
    }

    @Override
    public void resetValues(Map<String, Object> values) {
        RequestContextHolder.resetRequestAttributes();
    }
}
