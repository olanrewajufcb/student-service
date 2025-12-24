package com.emis.studentsservice.dto.request;

import com.emis.studentsservice.enums.EnrollmentType;
import java.time.LocalDateTime;

public record EnrollmentHistoryRequest(String schoolYear,
                                       String gradeLevel,
                                       String schoolName,
                                       Long schoolId,
                                       EnrollmentType type,
                                       String notes,
                                       LocalDateTime effectiveDate,
                                       LocalDateTime recordedAt) {}
