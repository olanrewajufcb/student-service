package com.emis.studentsservice.controller;

import com.emis.studentsservice.dto.response.AnalyticsSummaryResponse;
import com.emis.studentsservice.dto.response.StudentDropoutRiskResponse;
import com.emis.studentsservice.security.CanCreateResource;
import com.emis.studentsservice.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1/students")
public class StudentAnalyticsController {

    private final AnalyticsService analyticsService;
    @CanCreateResource
    @Operation(summary = "Get enrollment summary",
    description = "Get enrollment summary")
    @GetMapping("/analytics/enrollment-summary")
    public Mono<AnalyticsSummaryResponse> getEnrollmentSummary(
            @RequestParam String academicYear,
            @RequestHeader(value = "schoolCode", required = false) String schoolCode
            ) {

        log.info("Getting enrollment summary for schoolCode: {}, academicYear: {}", schoolCode, academicYear);
        String requestId = UUID.randomUUID().toString();

        return analyticsService.getEnrollmentSummary(schoolCode, academicYear, requestId)
                .contextWrite(ctx -> ctx.put("requestId", requestId));
    }

    @CanCreateResource
    @Operation(summary = "Get all schools enrollment summary",
            description = "Get all schools enrollment summary")
    @GetMapping("schools/analytics/enrollment-summary")
    public Flux<AnalyticsSummaryResponse> getAllSchoolsEnrollmentSummary(
            @RequestParam String academicYear,
            @RequestHeader(value = "schoolCode", required = false) String schoolCode
    ) {

        log.info("Getting enrollment summary for all schools: academicYear: " +
                "{} and schoolCode {}",  academicYear, schoolCode);
        String requestId = UUID.randomUUID().toString();

        return analyticsService.getEnrollmentSummary(academicYear, requestId)
                .contextWrite(ctx -> ctx.put("requestId", requestId));
    }

    @CanCreateResource
    @Operation(summary = "Get student dropout risk",
    description = "Get student droupout risk")
    @GetMapping("/analytics/student-dropout-risk")
    public Flux<StudentDropoutRiskResponse> getStudentDropoutRisk(
            @RequestParam String schoolCode) {

        log.info("Getting student dropout risk for schoolCode: {}", schoolCode);
        String requestId = UUID.randomUUID().toString();

        return analyticsService.getStudentDropoutRisk(schoolCode, requestId)
                .contextWrite(ctx -> ctx.put("requestId", requestId));
    }
}
