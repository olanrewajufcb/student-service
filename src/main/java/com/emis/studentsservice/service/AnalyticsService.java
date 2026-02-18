package com.emis.studentsservice.service;

import com.emis.studentsservice.dto.response.AnalyticsSummaryResponse;
import com.emis.studentsservice.dto.response.StudentDropoutRiskResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AnalyticsService {

    Mono<AnalyticsSummaryResponse> getEnrollmentSummary(
            String schoolCode, String academicYear,String requestId);

    Flux<AnalyticsSummaryResponse> getEnrollmentSummary(
             String academicYear, String requestId);

    Flux<StudentDropoutRiskResponse> getStudentDropoutRisk(String schoolCode,
                                                           String requestId);
}
