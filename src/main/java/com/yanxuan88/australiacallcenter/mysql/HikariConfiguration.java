package com.yanxuan88.australiacallcenter.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 配置hikari连接池
 *
 * @author co
 * @since 2023/11/30 上午9:50:38
 */
@Configuration
@EnableConfigurationProperties(HikariConfiguration.HikariProperties.class)
public class HikariConfiguration {
    private final Logger log = LoggerFactory.getLogger(HikariConfiguration.class);
    private final DataSourceProperties dataSourceProperties;
    private final HikariProperties hikariProperties;

    public HikariConfiguration(DataSourceProperties dataSourceProperties, HikariProperties hikariProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.hikariProperties = hikariProperties;
    }

    @Bean
    public DataSource dataSource() {
        log.info("初始化数据源，使用Hikari数据库连接池");
        hikariProperties.setJdbcUrl(dataSourceProperties.getUrl());
        hikariProperties.setUsername(dataSourceProperties.getUsername());
        hikariProperties.setPassword(dataSourceProperties.getPassword());
        hikariProperties.setDriverClassName(dataSourceProperties.getDriverClassName());
        return new HikariDataSource(hikariProperties);
    }

    @ConfigurationProperties("spring.datasource.hikari")
    public static class HikariProperties extends HikariConfig {

    }
}
