package com.yanxuan88.australiacallcenter.config;

import com.yanxuan88.australiacallcenter.instrumentation.TimingTracingInstrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQlConfig {
    @Bean
    public GraphQlSourceBuilderCustomizer sourceBuilderCustomizer() {
        return (builder) ->
                builder.configureGraphQl(graphQlBuilder ->
                        graphQlBuilder.instrumentation(new TimingTracingInstrumentation()));
    }
}
