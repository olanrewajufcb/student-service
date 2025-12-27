package com.emis.studentsservice.dto.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(
    String requestId,
    LocalDateTime timestamp,
    T data
) {
    public static <T> ApiResponse<T> of(String requestId, T data) {
        return new ApiResponse<>(requestId, LocalDateTime.now(), data);
    }
}