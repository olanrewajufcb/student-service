package com.emis.studentsservice.dto.request;

import com.emis.studentsservice.enums.ReportFormat;
import com.emis.studentsservice.enums.ReportType;

public record GenerateStudentReportRequest(
        String schoolCode,
        String academicYear,
        ReportType reportType,
        ReportFormat format
) {}
