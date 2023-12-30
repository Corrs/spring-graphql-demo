package com.yanxuan88.australiacallcenter.graphql;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.graphql.file")
public class GraphQLFileProperties {
    /**
     * 上传目录
     */
    private String dir;
}
