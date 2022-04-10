package com.github.freetie.course.annotation;

import com.github.freetie.course.entity.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RoleControl {
    Role role() default Role.ADMIN;
}
