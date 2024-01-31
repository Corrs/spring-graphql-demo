package com.yanxuan88.australiacallcenter.config;

import com.yanxuan88.australiacallcenter.graphql.MyExceptionResolver;
import com.yanxuan88.australiacallcenter.graphql.MyUploadGraphQlHttpHandler;
import com.yanxuan88.australiacallcenter.graphql.ValidationExceptionResolver;
import com.yanxuan88.australiacallcenter.graphql.instrumentation.TimingTracingInstrumentation;
import com.yanxuan88.australiacallcenter.graphql.scalar.ScalarRegisterConfigurer;
import graphql.ExecutionInput;
import graphql.com.google.common.collect.Lists;
import graphql.execution.preparsed.persisted.InMemoryPersistedQueryCache;
import graphql.execution.preparsed.persisted.PersistedQuerySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.graphql.execution.SecurityDataFetcherExceptionResolver;
import org.springframework.graphql.server.WebGraphQlHandler;
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
        return builder -> builder
                .exceptionResolvers(Lists.newArrayList(new SecurityDataFetcherExceptionResolver(), new MyExceptionResolver(), new ValidationExceptionResolver()))
                .configureGraphQl(graphQlBuilder -> graphQlBuilder
                        // 缓存文档，而不是缓存结果，
                        .preparsedDocumentProvider(new PersistedQuerySupport(InMemoryPersistedQueryCache.newInMemoryPersistedQueryCache().build()) {
                            @Override
                            protected Optional<Object> getPersistedQueryId(ExecutionInput executionInput) {
                                return Optional.ofNullable(Base64.getEncoder().encodeToString(executionInput.getQuery().getBytes(StandardCharsets.UTF_8)));
                            }
                        })
                        .instrumentation(new TimingTracingInstrumentation())
                )
                ;
    }

    @Bean
    @Order(1)
    public RouterFunction<ServerResponse> graphQlFileUploadRouterFunction(GraphQlProperties properties, WebGraphQlHandler webGraphQlHandler) {
        String path = properties.getPath();
        MyUploadGraphQlHttpHandler httpHandler = new MyUploadGraphQlHttpHandler(webGraphQlHandler);
        log.info("GraphQL file upload endpoint HTTP POST {}", path);
        return RouterFunctions.route(RequestPredicates.POST(path)
                .and(RequestPredicates.accept(SUPPORTED_MEDIA_TYPES))
                .and(RequestPredicates.contentType(SUPPORTED_MEDIA_TYPES)), httpHandler::handleRequest);
    }
}
