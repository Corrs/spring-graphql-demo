package com.yanxuan88.australiacallcenter.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yanxuan88.australiacallcenter.annotation.Authenticated;
import com.yanxuan88.australiacallcenter.graphql.util.RelayUtil;
import graphql.relay.Connection;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import javax.servlet.http.Part;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Controller
public class HelloController {
    @QueryMapping
//    @PreAuthorize("hasPermission(null, 'hello1')")
    @PreAuthorize("hasAuthority('hello1')")
    public String hello(@Argument String name) {
        return "hello " + name;
    }

    @Cacheable(cacheNames = "helloLocalDateTime#1m")
    @QueryMapping
    public LocalDateTime helloLocalDateTime() {
        return LocalDateTime.now();
    }

    @Cacheable(cacheNames = {"viewer#3m"})
    @QueryMapping
    public User viewer() {
        User user = new User();
        user.setId("VXNlcjpzb21lSWQ");
        return user;
    }

    @Authenticated
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

    @SubscriptionMapping
    public Publisher<String> greeting() {
        return Flux.fromStream(Stream.generate(() -> "Hello GraphQL " + UUID.randomUUID()))
                .delayElements(Duration.ofSeconds(1))
                .take(10);
    }
}
