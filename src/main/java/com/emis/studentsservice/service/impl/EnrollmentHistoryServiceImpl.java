package com.emis.studentsservice.service.impl;

import com.emis.studentsservice.domain.db.EnrollmentHistory;
import com.emis.studentsservice.domain.db.Student;
import com.emis.studentsservice.dto.request.EnrollmentHistoryRequest;
import com.emis.studentsservice.dto.response.EnrollmentResponse;
import com.emis.studentsservice.enums.EnrollmentType;
import com.emis.studentsservice.mapper.EnrollmentMapper;
import com.emis.studentsservice.repository.EnrollmentHistoryRepository;
import com.emis.studentsservice.service.EnrollmentHistoryService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class EnrollmentHistoryServiceImpl implements EnrollmentHistoryService {
    private final EnrollmentHistoryRepository enrollmentHistoryRepository;
    private  final EnrollmentMapper mapper;

    @Override
    public Mono<EnrollmentResponse> createInitialEnrollment(Student student, String schoolName) {
        var enrollmentRequest = new EnrollmentHistoryRequest(
            getCurrentSchoolYear(),
                student.getGradeLevel().name(),
                schoolName,
                student.getSchoolId(),
                EnrollmentType.INITIAL_ENROLLMENT,
                "Initial student enrollment",
                LocalDateTime.now(),
                LocalDateTime.now());
        return createEnrollment(student,  enrollmentRequest);

    }


    public Mono<EnrollmentResponse> createEnrollment(Student student, EnrollmentHistoryRequest request) {
        log.info("Creating enrollment history for student: {}", student);

        return Mono.defer(() -> createEnrollmentEntity(student, request))
                .flatMap(enrollmentHistoryRepository::save)
                .map(mapper::toResponse)
                .doOnSuccess(enrollment ->
                        log.info("Enrollment history created: {}", enrollment.enrollmentId()))
                .doOnError(error -> log.error("Failed to add enrollment history: {}", error.getMessage()));

    }



    private String getCurrentSchoolYear() {
        int currentYear = LocalDate.now().getYear();
        int nextYear = currentYear + 1;
        return currentYear + "-" + nextYear;
    }

    private Mono<EnrollmentHistory> createEnrollmentEntity(Student student, EnrollmentHistoryRequest request) {
    return Mono.fromCallable(
        () ->
            new EnrollmentHistory(
                null,
                student.getStudentId(),
                request.schoolYear(),
                request.gradeLevel(),
                request.schoolName(),
                student.getSchoolId(),
                request.type(),
                request.notes(),
                Boolean.FALSE,
                null,
                request.effectiveDate(),
                LocalDateTime.now()));
    }
}
