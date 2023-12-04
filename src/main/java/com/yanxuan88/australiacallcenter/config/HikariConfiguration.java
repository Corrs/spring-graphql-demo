package com.yanxuan88.australiacallcenter.config;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.Duration;

import static io.micrometer.core.instrument.config.validate.PropertyValidator.getDuration;

/**
 * 配置hikari连接池
 *
 * @author co
 * @since 2023/11/30 上午9:50:38
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({HikariProperties.class})
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
        hikariProperties.setMetricsTrackerFactory(initMicrometerMetricsTrackerFactory());
        HikariDataSource dataSource = new HikariDataSource(hikariProperties);
        return dataSource;
    }

    private MicrometerMetricsTrackerFactory initMicrometerMetricsTrackerFactory() {
        log.info("使用LoggingMeterRegistry构建Hikari数据连接池的MicrometerMetricsTrackerFactory");
        final LoggingMeterRegistry registry = LoggingMeterRegistry
                .builder(new LoggingRegistryConfig() {

                    @Override
                    public String get(String key) {
                        return null;
                    }

                    @Override
                    public Duration step() {
                        return getDuration(this, "step").orElse(Duration.ofMinutes(hikariProperties.getLoggingMeterPushMetricsStep()));
                    }
                })
                .threadFactory(new NamedThreadFactory("hikari-logging-metrics-publisher"))
                .build();
        return new MicrometerMetricsTrackerFactory(registry);
    }
}
