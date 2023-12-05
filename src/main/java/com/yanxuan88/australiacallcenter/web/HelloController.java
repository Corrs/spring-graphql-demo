package com.yanxuan88.australiacallcenter.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class HelloController {
    @Cacheable(cacheNames = {"query.hello"})
    @QueryMapping
    @PreAuthorize("hasPermission(null, 'hello')")
    public String hello(@Argument String name) {
        return "hello " + name;
    }

    @Cacheable(cacheNames = {"helloLDT"})
    @QueryMapping
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime helloLocalDateTime() {
        return LocalDateTime.now();
    }
}
