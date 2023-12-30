package com.yanxuan88.australiacallcenter.graphql;

import com.yanxuan88.australiacallcenter.exception.BizException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ValidationExceptionResolver extends DataFetcherExceptionResolverAdapter {
    public ValidationExceptionResolver() {
        setThreadLocalContextAware(true);
    }

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException e = (ConstraintViolationException) ex;
            final Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            String message = violations.stream().findFirst().map(ConstraintViolation::getMessage).get();
            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .message(message)
                    .build();
        }
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            BindingResult bindingResult = e.getBindingResult();
            List<ObjectError> errors = bindingResult.getAllErrors();
            String message = "";
            if (!errors.isEmpty()) {
                // 只显示第一个错误信息
                FieldError fieldError = (FieldError) errors.get(0);
//            message = fieldError.getField() + fieldError.getDefaultMessage();
                message = fieldError.getDefaultMessage();
            }
            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .message(message)
                    .build();
        }
        return null;
    }
}
