package com.yanxuan88.australiacallcenter.config;

import com.yanxuan88.australiacallcenter.graphql.instrumentation.TimingTracingInstrumentation;
import com.yanxuan88.australiacallcenter.graphql.scalar.ScalarRegisterConfigurer;
import graphql.ExecutionInput;
import graphql.execution.preparsed.persisted.InMemoryPersistedQueryCache;
import graphql.execution.preparsed.persisted.PersistedQuerySupport;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Configuration
public class GraphQlConfig {

    /**
     * 自定义类型注册配置器
     *
     * @return RuntimeWiringConfigurer
     */
    @Bean
    RuntimeWiringConfigurer scalarRegisterConfigurer() {
        return new ScalarRegisterConfigurer();
    }

    @Bean
    public GraphQlSourceBuilderCustomizer sourceBuilderCustomizer() {
        return (builder) ->
                builder.configureGraphQl(graphQlBuilder -> graphQlBuilder
                        // 缓存文档，而不是缓存结果，
                        // ApolloPersistedQuerySupport需要配合apollo-client使用 https://www.apollographql.com/docs/apollo-server/performance/apq/
                        // .preparsedDocumentProvider(new ApolloPersistedQuerySupport(InMemoryPersistedQueryCache.newInMemoryPersistedQueryCache().build()))
                        // 也可以自己去实现PersistedQuerySupport
                        .preparsedDocumentProvider(new PersistedQuerySupport(InMemoryPersistedQueryCache.newInMemoryPersistedQueryCache().build()) {
                            @Override
                            protected Optional<Object> getPersistedQueryId(ExecutionInput executionInput) {
                                return Optional.ofNullable(Base64.getEncoder().encodeToString(executionInput.getQuery().getBytes(StandardCharsets.UTF_8)));
                            }
                        })
                        .instrumentation(new TimingTracingInstrumentation())
                );
    }
}
