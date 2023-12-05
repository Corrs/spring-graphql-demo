package com.yanxuan88.australiacallcenter.config;

import com.yanxuan88.australiacallcenter.graphql.instrumentation.TimingTracingInstrumentation;
import com.yanxuan88.australiacallcenter.graphql.scalar.ScalarRegisterConfigurer;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

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
                builder.configureGraphQl(graphQlBuilder ->
                        graphQlBuilder.instrumentation(new TimingTracingInstrumentation())
                );
    }
}
