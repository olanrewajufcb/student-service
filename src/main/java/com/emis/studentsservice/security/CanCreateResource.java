package com.emis.studentsservice.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
"@schoolAuth.authorize(authentication, #p1, T(com.emis.studentsservice.enums.ResourceAction).CREATE_RESOURCE)"
)
public @interface CanCreateResource {}