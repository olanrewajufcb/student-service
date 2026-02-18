package com.emis.studentsservice.controller;

import com.emis.studentsservice.domain.db.Student;
import com.emis.studentsservice.dto.request.CreateStudentRequest;
import com.emis.studentsservice.dto.request.UpdateStudentRequest;
import com.emis.studentsservice.dto.response.ApiResponse;
import com.emis.studentsservice.dto.response.StudentResponse;
import com.emis.studentsservice.dto.response.StudentStatisticsResponse;
import com.emis.studentsservice.exception.BadRequestException;
import com.emis.studentsservice.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("api/v1/students")
public class StudentServiceController {

    private final StudentService studentService;
    private static final String REQUEST_ID = "requestId";
    private static final Set<String> ALLOWED_SORT_FIELDS = Arrays.stream(Student.class.getDeclaredFields())
            .map(Field::getName)
            .collect(Collectors.toSet());

    @Operation(summary = "Create a new student",
    description = "Create a new student")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        String requestId = UUID.randomUUID().toString();

        return studentService.createStudent(request)
                .doOnSubscribe(sub -> log.info("Creating student with id {}", requestId))
                .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));

    }

    @Operation(summary = "Get a student by number",
    description = "Get a student by number")
    @GetMapping("{studentNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<StudentResponse> getStudentByNumber(@PathVariable String studentNumber,
                                                    @RequestParam String schoolCode) {
    String requestId = UUID.randomUUID().toString();
    return studentService
        .getStudentByNumberAndSchoolCode(studentNumber, schoolCode,  requestId)
        .doOnSubscribe(sub -> log.info("Getting student details with id {}", requestId))
        .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));
  }

    @Operation(summary = "Update a student",
    description = "Update a student")
    @PutMapping("{studentNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<StudentResponse> updateStudent(@PathVariable String studentNumber,
                                               @Valid @RequestBody UpdateStudentRequest request) {
        String requestId = UUID.randomUUID().toString();

       return studentService.updateStudent(studentNumber, request, requestId)
                .doOnSubscribe(sub -> log.info("Updating student with id {}", requestId))
                .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));
    }

    @Operation(summary = "Get all students",
    description = "Get all students from a school")
    @GetMapping("all/{schoolCode}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<StudentResponse> getStudentsBySchoolCode(@PathVariable String schoolCode,
                        @RequestParam(defaultValue = "0")
                        @Min(value = 0, message = "page must not be less than 0")
                        int page,
                        @RequestParam(defaultValue = "10")
                        @Min(value = 1, message = "size must be at least 1")
                        int size,
                        @RequestParam(defaultValue = "studentNumber")
                        String sortBy) {

        if(!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new BadRequestException("Invalid sort field: " + sortBy);
        }

        var pageRequest = PageRequest.of(page, size, Sort.by(sortBy));

        String requestId = UUID.randomUUID().toString();
        return studentService.getStudentsBySchoolCode(schoolCode, pageRequest, requestId)
                .doOnSubscribe(sub -> log.info("Getting students for school with id {}", requestId))
                .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));
    }

    @PostMapping("/batch")
    public Flux<StudentResponse> batchCreateStudent(@RequestBody List<Long> studentId) {
        String requestId = UUID.randomUUID().toString();

        return studentService.getStudentsBatch(studentId, requestId)
                .doOnSubscribe(sub -> log.info("Getting students for school with id {}", requestId))
                .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));
    }

    @Operation(summary = "Get students statistics",
    description = "Get students statistics")
    @GetMapping("/statistics/{schoolCode}")
    public Mono<StudentStatisticsResponse> getStudentStatistics(@PathVariable String schoolCode) {
        String requestId = UUID.randomUUID().toString();

        return studentService.getStudentStatistics(schoolCode, requestId)
                .doOnSubscribe(sub -> log.info("Fetching students statistics for school with id {}", requestId))
                .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));
    }

    @Operation(summary = "Get all schools students statistics",
    description = "Get all schools students statistics")
    @GetMapping("schools/statistics")
    public Mono<ApiResponse<StudentStatisticsResponse>> getAllSchoolsStudentStatistics() {
        String requestId = UUID.randomUUID().toString();

        return studentService.getAllSchoolsStudentStatistics(requestId)
                .doOnSubscribe(sub -> log.info("Fetching student statistics for ALL schools [requestId={}]", requestId))
                .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));
    }

    @Operation(summary = "Get all students",
    description = "Get all students")
    @GetMapping
    public Mono<Page<StudentResponse>> getAllStudents(
            @RequestParam(defaultValue = "0")
                                                          @Min(value = 0, message = "page must not be less than 0")
                                                          int page,
                                                      @RequestParam(defaultValue = "10")
                                                          @Min(value = 1, message = "size must be at least 1")
                                                          int size,
                                                      @RequestParam(defaultValue = "studentNumber")
                                                      String sortBy){
        String requestId = UUID.randomUUID().toString();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return studentService.getAllStudents(pageable, requestId)
                .doOnSubscribe(sub -> log.info("Getting all students [requestId={}]", requestId))
                .contextWrite(ctx -> ctx.put(REQUEST_ID, requestId));

    }
}

