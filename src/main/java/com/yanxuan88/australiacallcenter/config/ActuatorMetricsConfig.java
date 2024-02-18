package com.yanxuan88.australiacallcenter.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorMetricsConfig {
    @Bean
    InitializingBean forcePostProcessor(BeanPostProcessor meterRegistryPostProcessor, MeterRegistry registry) {
        return () -> meterRegistryPostProcessor.postProcessAfterInitialization(registry, "");
    }
}
