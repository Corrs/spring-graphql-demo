package com.yanxuan88.australiacallcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.yanxuan88.australiacallcenter.mapper")
@SpringBootApplication
public class AustraliaCallCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AustraliaCallCenterApplication.class, args);
    }

}
