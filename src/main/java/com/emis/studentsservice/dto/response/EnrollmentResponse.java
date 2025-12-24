package com.emis.studentsservice.dto.response;

import com.emis.studentsservice.enums.EnrollmentType;
import java.time.LocalDateTime;

public record EnrollmentResponse(
        Long enrollmentId,
                                 String schoolYear,
                                 String gradeLevel,
                                 String schoolName,
                                 Long schoolId,
                                 EnrollmentType type,
                                 String note,
                                 LocalDateTime effectiveDate,
                                 LocalDateTime recordedAt) {}
