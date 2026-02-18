package com.emis.studentsservice.service.report;

import com.emis.studentsservice.dto.request.GenerateStudentReportRequest;
import com.emis.studentsservice.dto.response.GenerateStudentReportResponse;
import com.emis.studentsservice.dto.response.StudentReportDetailsResponse;
import reactor.core.publisher.Mono;

public interface StudentReportService {

    Mono<GenerateStudentReportResponse> generateReport(GenerateStudentReportRequest request,
                                                       String requestId);

    Mono<StudentReportDetailsResponse> getReport(Long reportId);
}
