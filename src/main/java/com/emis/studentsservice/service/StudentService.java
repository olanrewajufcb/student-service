package com.emis.studentsservice.service;

import com.emis.studentsservice.dto.request.CreateStudentRequest;
import com.emis.studentsservice.dto.request.UpdateStudentRequest;
import com.emis.studentsservice.dto.response.StudentResponse;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StudentService {

//    Mono<StudentResponse> createStudent(CreateStudentRequest request, UserContext context);
    Mono<StudentResponse> createStudent(CreateStudentRequest request);

    Mono<StudentResponse>  updateStudent(String studentNumber, UpdateStudentRequest request, String requestId);
    Flux<StudentResponse> getStudentsBySchoolCode(String schoolCode, Pageable pageable, String requestId);
    Mono<StudentResponse> getStudentByNumberAndSchoolCode(String studentNumber, String schoolCode, String requestId);

    Flux<StudentResponse> getStudentsBatch(List<Long> studentIds, String requestId);


}
