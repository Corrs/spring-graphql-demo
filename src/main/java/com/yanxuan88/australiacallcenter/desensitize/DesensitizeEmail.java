package com.yanxuan88.australiacallcenter.desensitize;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 脱敏注解
 *
 * @author co
 * @since 2024-01-09 16:12:40
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@Desensitize(desensitization = DesensitizationEmail.class)
public @interface DesensitizeEmail {
    @AliasFor(attribute = "ignore", annotation = Desensitize.class)
    String ignore() default "";
}
