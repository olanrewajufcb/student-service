package com.emis.studentsservice.dto.response;


import com.emis.studentsservice.domain.StudentReport;

public record StudentReportDetailsResponse(
        Long reportId,
        String reportType,
        String status,
        String fileUrl
) {
    public static StudentReportDetailsResponse from(StudentReport report){
        return new StudentReportDetailsResponse(
                report.getReportId(),
                report.getReportType(),
                report.getGenerationStatus(),
                report.getFilePath()
        );
    }
}
