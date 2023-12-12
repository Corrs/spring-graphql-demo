package com.yanxuan88.australiacallcenter.graphql.scalar;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

/**
 * GraphQL自定义类型注册
 *
 * @author co
 * @since 2023-12-05 11:22:09
 */
public class ScalarRegisterConfigurer implements RuntimeWiringConfigurer {
    @Override
    public void configure(RuntimeWiring.Builder builder) {
        builder.scalar(GraphQLScalarType.newScalar().name("LocalDateTime").coercing(new LocalDateTimeScalar()).build())
                .scalar(GraphQLScalarType.newScalar().name("Upload").coercing(new UploadScalar()).build())
                .scalar(ExtendedScalars.GraphQLLong);
    }
}
