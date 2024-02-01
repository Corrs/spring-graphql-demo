package com.yanxuan88.australiacallcenter.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.execution.SubscriptionExceptionResolverAdapter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

public class SecuritySubscriptionExceptionResolver extends SubscriptionExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex) {
        if (ex instanceof AccessDeniedException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.FORBIDDEN)
                    .message("Forbidden")
                    .build();
        }
        if (ex instanceof AuthenticationException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.UNAUTHORIZED)
                    .message("Unauthorized")
                    .build();
        }
        return null;
    }
}
