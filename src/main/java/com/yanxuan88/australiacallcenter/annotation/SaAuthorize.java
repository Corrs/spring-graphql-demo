package com.yanxuan88.australiacallcenter.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("isAuthenticated() && authentication?.credentials?.userId == T(com.yanxuan88.australiacallcenter.common.Constant).SUPER_ADMIN_USER_ID")
public @interface SaAuthorize {
}
