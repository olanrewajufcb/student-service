package com.emis.studentsservice.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "student_reports", schema = "student_schema")
public class StudentReport {
    @Id
    private Long reportId;
    private Long schoolId;
    private String schoolCode;
    private String academicYear;
    private String reportType;
    private String reportFormat;
    private String generationStatus;
    private String filePath;
    private String requestedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
