package com.yanxuan88.australiacallcenter.graphql.instrumentation;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * 计时
 *
 * @author co
 * @since 2023/11/30 下午4:54:49
 */
@Slf4j
@Component
public class TimingTracingInstrumentation extends SimpleInstrumentation {
    @Override
    public InstrumentationState createState() {
        return new TracingState();
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        TracingState tracingState = parameters.getInstrumentationState();
        tracingState.startTime = System.currentTimeMillis();
        return super.beginExecution(parameters);
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        // We only care about user code
        if (parameters.isTrivialDataFetcher()) {
            return dataFetcher;
        }

        return environment -> {
            String datafetcherTag = findDatafetcherTag(parameters);
            log.info("Datafetcher {} begin execution", datafetcherTag);
            long startTime = System.currentTimeMillis();
            Object result = dataFetcher.get(environment);
            if (result instanceof CompletableFuture) {
                ((CompletableFuture<?>) result).whenComplete((r, ex) -> {
                    long totalTime = System.currentTimeMillis() - startTime;
                    log.info("Async datafetcher {} took {}ms", datafetcherTag, totalTime);
                });
            } else {
                long totalTime = System.currentTimeMillis() - startTime;
                log.info("Datafetcher {} took {}ms", datafetcherTag, totalTime);
            }

            return result;
        };
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        HashMap<Object, Object> extensions = new HashMap<>();
        if (executionResult.getExtensions() != null) {
            extensions.putAll(executionResult.getExtensions());
        }

        /*Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("myHeader", "hello");
        extensions.put("responseHeaders", responseHeaders);*/
        TracingState tracingState = parameters.getInstrumentationState();
        long totalTime = System.currentTimeMillis() - tracingState.startTime;
        log.info("Total execution time: {}ms", totalTime);

        return super.instrumentExecutionResult(new ExecutionResultImpl(executionResult.getData(), executionResult.getErrors(), extensions), parameters);
    }

    private String findDatafetcherTag(InstrumentationFieldFetchParameters parameters) {
        GraphQLOutputType type = parameters.getExecutionStepInfo().getParent().getType();
        GraphQLObjectType parent;
        if (type instanceof GraphQLNonNull) {
            parent = (GraphQLObjectType) ((GraphQLNonNull) type).getWrappedType();
        } else {
            parent = (GraphQLObjectType) type;
        }

        return parent.getName() + "." + parameters.getExecutionStepInfo().getPath().getSegmentName();
    }

    static class TracingState implements InstrumentationState {
        long startTime;
    }
}
