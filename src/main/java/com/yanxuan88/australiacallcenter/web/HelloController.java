package com.yanxuan88.australiacallcenter.web;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class HelloController {
    @QueryMapping
    @PreAuthorize("hasPermission(null, 'hello')")
    public String hello(@Argument String name) {
        return "hello " + name;
    }
}
