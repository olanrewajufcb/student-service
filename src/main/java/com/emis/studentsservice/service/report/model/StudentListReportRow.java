package com.emis.studentsservice.service.report.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
public class StudentListReportRow {
    private String schoolCode;
    private String schoolName;
    private String academicYear;
    private String studentNumber;
    private Long studentId;
    private String fullName;
    private String status;
}
