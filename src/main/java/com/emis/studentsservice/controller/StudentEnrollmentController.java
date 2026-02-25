package com.emis.studentsservice.controller;


import com.emis.studentsservice.dto.request.PromotionRequest;
import com.emis.studentsservice.dto.request.StudentDropoutRequest;
import com.emis.studentsservice.dto.request.StudentEnrollmentRequest;
import com.emis.studentsservice.dto.request.StudentTransferRequest;
import com.emis.studentsservice.dto.response.PromotionResponse;
import com.emis.studentsservice.dto.response.StudentDropoutResponse;
import com.emis.studentsservice.dto.response.StudentEnrollmentResponse;
import com.emis.studentsservice.service.StudentEnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class StudentEnrollmentController {

    private final StudentEnrollmentService studentEnrollmentService;

    @Operation(summary = "Create a new student enrollment",
    description = "Create a new student enrollment")
    @PostMapping("/{studentNumber}/enrollments")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<StudentEnrollmentResponse> createStudentEnrollment(
            @PathVariable String studentNumber,
            @RequestBody StudentEnrollmentRequest studentEnrollmentRequest) {
        log.info("Creating student enrollment for student {}", studentNumber);
        String requestId = UUID.randomUUID().toString();
        return studentEnrollmentService
                .createStudentEnrollment(studentNumber, studentEnrollmentRequest, requestId)
                .contextWrite(ctx -> ctx.put("requestId", requestId));
    }

    @Operation(summary = "Get a student enrollment",
    description = "Get a student enrollment")
    @GetMapping("/{studentNumber}/enrollments/current")
    @ResponseStatus(HttpStatus.OK)
    public Mono<StudentEnrollmentResponse> getStudentEnrollment(
            @PathVariable String studentNumber,
            @RequestParam() String academicYear,
            @RequestParam() String schoolCode) {
        log.info("Getting student enrollment for student {} and {}", studentNumber, schoolCode);
        String requestId = UUID.randomUUID().toString();
        return studentEnrollmentService.getStudentEnrollment(studentNumber,schoolCode, academicYear, requestId)
                .contextWrite(ctx -> ctx.put("requestId", requestId));
    }

    @Operation(summary = "Transfer a student to another school",
    description = "Transfer a student to another school")
    @PostMapping("/{studentNumber}/transfer")
    @ResponseStatus(HttpStatus.OK)
    public Mono<StudentEnrollmentResponse> transferStudent(
            @PathVariable String studentNumber,
            @RequestBody StudentTransferRequest studentTransferRequest) {
        log.info("Transferring student {} to another school", studentNumber);
        String requestId = UUID.randomUUID().toString();
        return studentEnrollmentService
                .transferStudent(studentNumber, studentTransferRequest, requestId)
                .contextWrite(ctx -> ctx.put("requestId", requestId));
    }
    @Operation(summary = "Dropout a student from school",
    description = "Dropout a student from school")
    @PostMapping("/{studentNumber}/dropout")
    @ResponseStatus(HttpStatus.OK)
    public Mono<StudentDropoutResponse> dropoutStudent(
            @PathVariable String studentNumber,
            @RequestBody @Valid StudentDropoutRequest request) {
        log.info("Dropping out student {} from school", studentNumber);
        String requestId = UUID.randomUUID().toString();
        return studentEnrollmentService
                .dropoutStudent(studentNumber, request, requestId)
                .contextWrite(ctx -> ctx.put("requestId", requestId));
    }

    @Operation(summary = "Get all students enrollments in a school",
            description = "Get all student enrollments")
    @GetMapping("/enrollments/current")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Page<StudentEnrollmentResponse>> getAllStudentEnrollments(
            @RequestParam() String academicYear,
            @RequestParam() String schoolCode,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "page must not be less than 0")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "size must be at least 1")
            int size,
            @RequestParam(defaultValue = "studentNumber")
            String sortBy
            ) {
        log.info("Getting student enrollment for students in {}", schoolCode);
        String requestId = UUID.randomUUID().toString();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return studentEnrollmentService.getAllStudentEnrollments(schoolCode, academicYear, pageable, requestId)
                .contextWrite(ctx -> ctx.put("requestId", requestId));
    }

}
