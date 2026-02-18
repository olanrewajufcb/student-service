package com.emis.studentsservice.dto.request;

import java.time.LocalDate;

public record StudentTransferRequest(
        String fromSchoolCode,
        String toSchoolCode,
        String academicYear,
        LocalDate transferDate
) {}
