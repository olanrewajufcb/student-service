package com.emis.studentsservice.service;

import com.emis.studentsservice.domain.db.Student;
import com.emis.studentsservice.dto.request.EnrollmentHistoryRequest;
import com.emis.studentsservice.dto.response.EnrollmentResponse;
import reactor.core.publisher.Mono;

public interface EnrollmentHistoryService {

    Mono<EnrollmentResponse> createInitialEnrollment(Student student,  String schoolName);

    Mono<EnrollmentResponse> createEnrollment(Student student, EnrollmentHistoryRequest request);
}
