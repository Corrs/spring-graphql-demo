package com.yanxuan88.australiacallcenter.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.collect.Lists;
import com.yanxuan88.australiacallcenter.graphql.util.RelayUtil;
import graphql.relay.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
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

    @QueryMapping
    public User viewer() {
        User user = new User();
        user.setId("VXNlcjpzb21lSWQ");
        return user;
    }

    @SchemaMapping(typeName="User")
    public Connection<Todo> todos(@Argument Integer current, @Argument Integer size) {
        log.info("{}-{}", current, size);
        Page<Todo> todoPage = new Page<>(1, 10, 1);
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setText("test text");
        todoPage.setRecords(Lists.newArrayList(todo));
        return RelayUtil.build(todoPage);
    }
}
