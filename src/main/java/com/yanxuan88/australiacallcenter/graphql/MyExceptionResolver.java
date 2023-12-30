package com.yanxuan88.australiacallcenter.graphql;

import com.yanxuan88.australiacallcenter.exception.BizException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;

import java.util.Collections;

public class MyExceptionResolver extends DataFetcherExceptionResolverAdapter {
    public MyExceptionResolver() {
        setThreadLocalContextAware(true);
    }

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof BizException) {
            BizException e = (BizException) ex;

            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .message(e.getMsg())
                    .extensions(Collections.singletonMap("errCode", e.getCode()))
                    .build();
        }
        return null;
    }
}
