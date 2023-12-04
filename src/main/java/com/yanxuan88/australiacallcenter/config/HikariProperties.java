package com.yanxuan88.australiacallcenter.config;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * hikari连接池配置信息
 *
 * @author co
 * @since 2023/11/30 上午9:51:02
 */
@ConfigurationProperties("spring.datasource.hikari")
public class HikariProperties extends HikariConfig {
    @Value("${loggingMeterPushMetricsStep:15}")
    private int loggingMeterPushMetricsStep;

    public int getLoggingMeterPushMetricsStep() {
        return loggingMeterPushMetricsStep;
    }

    public void setLoggingMeterPushMetricsStep(int loggingMeterPushMetricsStep) {
        this.loggingMeterPushMetricsStep = loggingMeterPushMetricsStep;
    }
}
