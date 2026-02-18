package com.emis.studentsservice.dto.request;

public record StudentDropoutRequest(
        String academicYear,
        String schoolCode,
        String reason,
        String remarks
) {}
