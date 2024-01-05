package com.yanxuan88.australiacallcenter.config.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize(value = "isAuthenticated()")
public @interface Authenticated {
}
