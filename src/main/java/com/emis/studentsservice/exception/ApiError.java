package com.emis.studentsservice.exception;

import java.util.Map;
import lombok.Builder;

@Builder
public class ApiError {
    private String code;
    private String message;
    private Map<String, String> details;
}
