package com.emis.studentsservice.dto.response;

public record StudentDetails(
        Long studentId,
        String studentNumber,
        Integer absentDays,
        String riskLevel
) {}
