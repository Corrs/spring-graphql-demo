package com.yanxuan88.australiacallcenter.config;

import com.yanxuan88.australiacallcenter.graphql.MyGraphQlHttpHandler;
import com.yanxuan88.australiacallcenter.graphql.instrumentation.TimingTracingInstrumentation;
import com.yanxuan88.australiacallcenter.graphql.scalar.ScalarRegisterConfigurer;
import graphql.ExecutionInput;
import graphql.execution.preparsed.persisted.InMemoryPersistedQueryCache;
import graphql.execution.preparsed.persisted.PersistedQuerySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.server.webmvc.GraphQlHttpHandler;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Configuration
public class GraphQlConfig {
    private static MediaType[] SUPPORTED_MEDIA_TYPES = new MediaType[]{MediaType.MULTIPART_FORM_DATA};

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

    @Bean
    public GraphQlHttpHandler graphQlHttpHandler(WebGraphQlHandler webGraphQlHandler) {
        return new MyGraphQlHttpHandler(webGraphQlHandler);
    }

    @Bean
    public RouterFunction<ServerResponse> graphQlFileUploadRouterFunction(GraphQlProperties properties, GraphQlHttpHandler httpHandler) {
        String path = properties.getPath();
        log.info("GraphQL file upload endpoint HTTP POST {}", path);
        return RouterFunctions.route(RequestPredicates.POST(path)
                .and(RequestPredicates.accept(SUPPORTED_MEDIA_TYPES))
                .and(RequestPredicates.contentType(SUPPORTED_MEDIA_TYPES)), httpHandler::handleRequest);
    }
}
