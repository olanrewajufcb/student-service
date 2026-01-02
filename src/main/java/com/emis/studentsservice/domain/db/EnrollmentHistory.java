package com.emis.studentsservice.domain.db;

import com.emis.studentsservice.enums.EnrollmentType;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("enrolment_history")
public record EnrollmentHistory(
        @Id Long enrollmentId,
                                Long studentId,
                                String schoolYear,
                                String gradeLevel,
                                String schoolName,
                                Long schoolId,
                                EnrollmentType type,
                                String note,
                                Boolean isDeleted,
                                LocalDateTime deletedAt,
                                LocalDateTime effectiveDate,
                                LocalDateTime recordedAt) {}
