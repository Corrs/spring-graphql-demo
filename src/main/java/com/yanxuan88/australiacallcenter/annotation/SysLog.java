package com.yanxuan88.australiacallcenter.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value();
    boolean saveToDB() default true;
}
