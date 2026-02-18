package com.emis.studentsservice.controller;

import com.emis.studentsservice.dto.request.GenerateStudentReportRequest;
import com.emis.studentsservice.dto.response.*;
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

    @PostMapping("/reports")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<GenerateStudentReportResponse> generateReport(
            @RequestBody @Valid GenerateStudentReportRequest request
    ) {
        String requestId = UUID.randomUUID().toString();
        return service.generateReport(request, requestId)
                .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));
    }

    @GetMapping("/reports/{reportId}")
    public Mono<StudentReportDetailsResponse> getReport(
            @PathVariable Long reportId
    ) {
        return service.getReport(reportId);
    }
}
