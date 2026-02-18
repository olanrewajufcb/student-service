package com.emis.studentsservice.dto.request;

import com.emis.studentsservice.enums.ProgressionStatus;

public record PromotionRequest(
        String fromAcademicYear,
        String toAcademicYear,
        Long nextClassId,
        String className,
        String schoolCode,
        ProgressionStatus progressionStatus,
        String remarks
) {}
