package com.emis.studentsservice.dto.response;

import java.time.LocalDateTime;

public record GenerateStudentReportResponse(
        Long reportId,
        String status,
        LocalDateTime requestedAt
) {}
