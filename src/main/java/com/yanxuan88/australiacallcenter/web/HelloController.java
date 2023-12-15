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
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
public class HelloController {
    @Cacheable(cacheNames = {"query.hello"})
    @QueryMapping
    @PreAuthorize("hasAuthority('hello')")
    public String hello(@Argument String name) {
        return "hello " + name;
    }

    @Cacheable(cacheNames = {"helloLDT"})
    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public LocalDateTime helloLocalDateTime() {
        return LocalDateTime.now();
    }

    @QueryMapping
    public User viewer() {
        User user = new User();
        user.setId("VXNlcjpzb21lSWQ");
        return user;
    }

    @SchemaMapping(typeName = "User")
    public Connection<Todo> todos(@Argument Integer current, @Argument Integer size) {
        log.info("{}-{}", current, size);
        Page<Todo> todoPage = new Page<>(1, 10, 1);
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setText("test text");
        todoPage.setRecords(Lists.newArrayList(todo));
        return RelayUtil.build(todoPage);
    }

    @MutationMapping
    public boolean upload1(@Argument UploadDTO input) {
        log.info("{}", input);
        return false;
    }

    @MutationMapping
    public boolean upload2(@Argument Integer id, @Argument List<Part> input) {
        log.info("{}-{}", id, input);
        input.forEach(f -> {
            try {
                f.write("/Volumes/k1/logs/" + f.getSubmittedFileName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return false;
    }
}
