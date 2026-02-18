package com.emis.studentsservice.dto.request;

import com.emis.studentsservice.enums.EnrollmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentEnrollmentRequest(
        @NotBlank
        String academicYear,
        @NotNull
        EnrollmentType enrollmentType) {}
