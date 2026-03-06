package com.emis.studentsservice.security;@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
"@schoolAuth.authorize(authentication, #request.schoolCode, T(com.emis.schoolservice.enums.SchoolAction).CREATE_SCHOOL)"
)
public @interface CanCreateSchool {}