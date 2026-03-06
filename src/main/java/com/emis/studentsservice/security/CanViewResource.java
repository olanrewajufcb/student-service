package com.emis.studentsservice.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
        "@schoolAuth.authorize(authentication, #schoolCode, T(com.emis.studentsservice.enums.ResourceAction).VIEW_RESOURCE)"
)
public @interface CanViewResource {}
