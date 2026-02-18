package com.emis.studentsservice.service;

import com.emis.studentsservice.dto.request.PromotionRequest;
import com.emis.studentsservice.dto.request.StudentDropoutRequest;
import com.emis.studentsservice.dto.request.StudentEnrollmentRequest;
import com.emis.studentsservice.dto.request.StudentTransferRequest;
import com.emis.studentsservice.dto.response.PromotionResponse;
import com.emis.studentsservice.dto.response.StudentDropoutResponse;
import com.emis.studentsservice.dto.response.StudentEnrollmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface StudentEnrollmentService {

    Mono<StudentEnrollmentResponse> createStudentEnrollment(String studentNumber,
       StudentEnrollmentRequest request, String requestId);

    Mono<StudentEnrollmentResponse> getStudentEnrollment(
            String studentNumber,String schoolCode, String academicYear, String requestId);

    Mono<StudentEnrollmentResponse> transferStudent(String studentNumber,
                                                    StudentTransferRequest request,
       String requestId);

    Mono<PromotionResponse> promoteStudent(String studentNumber,
                                           PromotionRequest request,
                                           String requestId);

    Mono<StudentDropoutResponse> dropoutStudent(String studentNumber,
                                                StudentDropoutRequest request,
                                                String requestId);

    Mono<Page<StudentEnrollmentResponse>> getAllStudentEnrollments(
            String schoolCode, String academicYear, Pageable pageable, String requestId);
}
