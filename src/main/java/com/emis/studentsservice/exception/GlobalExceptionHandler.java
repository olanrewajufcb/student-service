package com.emis.studentsservice.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SchoolNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleSchoolNotFound(SchoolNotFoundException ex) {
        return ApiError.builder()
            .code("SCHOOL_NOT_FOUND")
            .message(ex.getMessage())
            .details(Map.of("schoolId", ex.getMessage()))
            .build();
    }

    @ExceptionHandler(SchoolServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiError handleSchoolServiceDown(SchoolServiceUnavailableException ex) {
        return ApiError.builder()
            .code("SCHOOL_SERVICE_DOWN")
            .message("School service is temporarily unavailable")
            .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(BadRequestException ex) {
        return ApiError.builder()
            .code("400")
            .message(ex.getMessage())
            .build();
        }
}