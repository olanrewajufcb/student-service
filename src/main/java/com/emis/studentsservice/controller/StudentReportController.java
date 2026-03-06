package com.emis.studentsservice.controller;

import com.emis.studentsservice.dto.request.GenerateStudentReportRequest;
import com.emis.studentsservice.dto.response.*;
import com.emis.studentsservice.security.CanCreateResource;
import com.emis.studentsservice.security.CanViewResource;
import com.emis.studentsservice.service.report.StudentReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;


@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1/students")
public class StudentReportController {

    private static final String REQUEST_ID = "requestId";
    private final StudentReportService service;

    @CanCreateResource
    @PostMapping("/reports")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<GenerateStudentReportResponse> generateReport(
            @RequestBody @Valid GenerateStudentReportRequest request,
            @RequestHeader(value = "schoolCode", required = false) String schoolCode
    ) {
        String requestId = UUID.randomUUID().toString();
        log.info("Generating report for schoolCode: {} and {}", schoolCode, request);
        return service.generateReport(request, requestId)
                .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));
    }

    @CanViewResource
    @GetMapping("/reports/{reportId}")
    public Mono<StudentReportDetailsResponse> getReport(
            @PathVariable Long reportId,
            @RequestHeader(value = "schoolCode", required = false) String schoolCode
    ) {
        log.info("Getting report details for reportId: {} and {}", reportId, schoolCode);
        return service.getReport(reportId);
    }
}
