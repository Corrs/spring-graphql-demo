package com.yanxuan88.australiacallcenter.mysql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;

import javax.sql.DataSource;

@Configuration
@Import({JdbcLockClient.class})
public class JdbcSIConfiguration {

    @Bean
    public DefaultLockRepository defaultLockRepository(DataSource dataSource) {
        return new DefaultLockRepository(dataSource);
    }

    @Bean
    public JdbcLockRegistry jdbcLockRegistry(DefaultLockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }
}
