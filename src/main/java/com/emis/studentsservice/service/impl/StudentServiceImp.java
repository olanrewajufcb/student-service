package com.emis.studentsservice.service.impl;

import com.emis.studentsservice.domain.db.Student;
import com.emis.studentsservice.dto.request.*;
import com.emis.studentsservice.dto.response.ApiResponse;
import com.emis.studentsservice.dto.response.SchoolDetailsResponse;
import com.emis.studentsservice.dto.response.StudentResponse;
import com.emis.studentsservice.dto.response.StudentStatisticsResponse;
import com.emis.studentsservice.enums.SchoolStatus;
import com.emis.studentsservice.enums.StudentStatus;
import com.emis.studentsservice.exception.*;
import com.emis.studentsservice.helper.StudentServiceHelper;
import com.emis.studentsservice.mapper.StudentMapper;
import com.emis.studentsservice.repository.KeyCountProjection;
import com.emis.studentsservice.repository.StudentRepository;
import com.emis.studentsservice.service.*;
import com.emis.studentsservice.service.client.SchoolService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImp implements StudentService {

    private final StudentRepository studentRepository;
    private final GuardianService guardianService;
    private final MedicalRecordService medicalService;
    private final EnrollmentHistoryService enrollmentService;
    private final SchoolService schoolService;
    private final TransactionalOperator transactionalOperator;
    private final StudentMapper studentMapper;
    private final StudentServiceHelper studentServiceHelper;



    @Override
    public Mono<StudentResponse> createStudent(CreateStudentRequest request) {
        Student student = studentMapper.toEntity(request);
        log.info("Creating student with number: {}", request.studentNumber());
        log.info("Fetching school details for school code: {}", request.schoolCode());
                return Mono.defer(() -> getSchoolDetails(request.schoolCode()))
                .flatMap(schoolDetails ->
                        validateSchoolCapacity(schoolDetails)
                        .thenReturn(schoolDetails)
                )
                        .flatMap(schoolDetails ->
                                Mono.defer(() -> {
                                    student.setSchoolName(schoolDetails.schoolName());
                                    return studentRepository.save(student)
                                            .flatMap(savedStudent -> {
                                                log.info("Student saved with ID: {}", savedStudent.getStudentId());
                                                return saveAssociatedData(savedStudent, request, schoolDetails.schoolName())
                                                    .thenReturn(savedStudent);
                                            });
                                        })
                                .as(transactionalOperator::transactional))
                .map(studentMapper::toResponse)
                .doOnSuccess(response -> log.info("Successfully created student: {}", response.studentId()))
                .doOnError(error -> log.error("Failed to create student:::::::::::", error))
                .onErrorMap(err -> {
                    if (err instanceof DataIntegrityViolationException) {
                        String msg = err.getMessage();
                        if(msg != null && msg.contains("student_number_key")) {
                            return new AlreadyExistException("Student with number '" + request.studentNumber() + "' already exists.");
                        }else if(msg != null && msg.contains("school_id")) {
                            return new ArgumentIsNullException("SchoolId is required and must reference an existing school." + request.schoolId());
                        }else {
                            return new AlreadyExistException("Data integrity violation: " + msg);
                        }
                    }
                    return new StudentCreationFailedException("Failed to complete creating student: " + err.getMessage());
                });
    }

    @Override
    public Mono<StudentResponse> updateStudent(String studentNumber, UpdateStudentRequest request,
                                       String requestId) {
    return studentRepository
        .findByStudentNumber(studentNumber)
        .switchIfEmpty(
            Mono.error(
                new StudentNotFoundException(
                    "Student with number '" + studentNumber + "' not found. " + requestId)))
        .flatMap(
            existingStudent -> {
              log.info("Found existing student with number: {}", studentNumber);
              if (existingStudent.getStatus() == StudentStatus.INACTIVE) {
                  return Mono.error(
                      new StudentInactiveException(
                          "Student with number '"
                              + studentNumber
                              + "' is inactive. "
                              + requestId));
              }
                Student updatedStudent =
                    studentServiceHelper.updateStudent(existingStudent, request);
                return studentRepository.save(updatedStudent);

            })
        .doOnSuccess(
            response -> log.info("Successfully updated student: {}", response.getStudentId()))
        .map(studentMapper::toResponse)
        .doOnError(error -> log.error("Failed to update student: {}", error.getMessage()))
        .onErrorMap(
            err -> {
              if (err instanceof DataIntegrityViolationException) {
                String msg = err.getMessage();
                return new AlreadyExistException("Data integrity violation: " + msg + requestId);
              }
              return new StudentUpdateFailedException(
                  "RequestId: " + requestId + err.getMessage() + studentNumber);
            });
    }

    @Override
    public Flux<StudentResponse> getStudentsBySchoolCode(String schoolCode,
                                         Pageable pageable, String requestId) {

              return studentRepository.findBySchoolCode(schoolCode,pageable.getPageSize(), pageable.getOffset())
                  .switchIfEmpty(
                      Mono.error(
                          new StudentNotFoundException(
                              "No students found for the the given school"
                                  + schoolCode
                                  + ". "
                                  + requestId)))
        .map(studentMapper::toResponse)
        .doOnSubscribe(
            sub -> log.info(" [{}] Fetching students for school code: {}", requestId, schoolCode))
        .doOnComplete(
            () -> log.info("Successfully retrieved students for school code: {}", schoolCode))
        .onErrorMap(
            error -> {
              log.error("Failed to retrieve students: {}", error.getMessage());
              throw new StudentNotFoundException(error.getMessage());
            });
    }

    public Mono<Void> deleteStudent(Long studentId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Mono<StudentResponse> getStudentByNumberAndSchoolCode(String studentNumber,
                                   String schoolCode, String requestId) {

        return studentRepository.findByStudentNumberAndSchoolCode(studentNumber,  schoolCode)
                .switchIfEmpty(Mono.error(new StudentNotFoundException(
                        "Student with number '" + studentNumber + "' not found.")))
                .map(studentMapper::toResponse)
                .doOnSuccess(student -> log.info("Retrieved student with number '{}'.", studentNumber))
                .doOnError(error -> log.error("Failed to retrieve student: {}", error.getMessage()));
    }

    @Override
    public Flux<StudentResponse> getStudentsBatch(List<Long> studentIds, String requestId) {
        return studentRepository.findAllById(studentIds)
                .map(studentMapper::toResponse);
    }

    @Override
    public Mono<StudentStatisticsResponse> getStudentStatistics(String schoolCode, String requestId) {
    return studentRepository
        .findSchoolIdBySchoolCode(schoolCode)
        .switchIfEmpty(Mono.error(new StudentNotFoundException("School not found: " + schoolCode)))
        .flatMap(
            schoolId ->
                Mono.zip(
                        studentRepository.countBySchoolId(schoolId),
                        studentRepository.countBySchoolIdAndStatus(schoolId),
                        toCountMap(studentRepository.countByStatusGrouped(schoolId)),
                        toCountMap(studentRepository.countByGradeLevelGrouped(schoolId)),
                        toCountMap(studentRepository.countByGenderGrouped(schoolId))
                ))
                    .map(tuple -> new StudentStatisticsResponse(
                            tuple.getT1(),
                            tuple.getT2(),
                            tuple.getT3(),
                            tuple.getT4(),
                            tuple.getT5()
                    ))
            .doOnSuccess(resp ->
                    log.info("[{}] Fetched stats: total={}, active={}", requestId,
                            resp.totalStudents(), resp.activeStudents()));
    }

    @Override
    public Mono<ApiResponse<StudentStatisticsResponse>> getAllSchoolsStudentStatistics(String requestId) {
        return Mono.zip(
                        studentRepository.countAllStudents(),
                        studentRepository.countAllStudentsByStatus("ACTIVE"),
                        toCountMap(studentRepository.countByStatusGrouped()),
                        toCountMap(studentRepository.countByGradeLevelGrouped()),
                        toCountMap(studentRepository.countByGenderGrouped()))
                .map(
                        tuple -> {
                            long totalStudents = tuple.getT1();
                            long activeStudents = tuple.getT2();
                            Map<String, Long> byStatus = tuple.getT3();
                            Map<String, Long> byGradeLevel = tuple.getT4();
                            Map<String, Long> byGender = tuple.getT5();
                           var response = new StudentStatisticsResponse(
                                    totalStudents, activeStudents, byStatus, byGradeLevel, byGender);
                            return new ApiResponse<>(requestId, LocalDateTime.now(), response);
                        })
                .doOnSuccess(resp -> log.info("[{}] Fetched stats: total={}, active={}",
                        requestId, resp.data().totalStudents(), resp.data().activeStudents()));
    }

    @Override
    public Mono<Page<StudentResponse>> getAllStudents(Pageable pageable, String requestId) {
        int size = pageable.getPageSize();
        long offset = pageable.getOffset();
    return Mono.zip(
            studentRepository.findAllStudents(size, offset).collectList(),
            studentRepository.countAllStudents())
        .timeout(Duration.ofSeconds(3))
        .map(
            tuple -> {
              List<Student> students = tuple.getT1();
              long totalCount = tuple.getT2();
                List<StudentResponse> response = totalCount == 0
                        ? List.of()
                        : students.stream()
                            .map(studentMapper::toResponse)
                            .toList();

              return (Page<StudentResponse>) new PageImpl<>(response, pageable, totalCount);
            })
        .doOnSuccess(resp -> log.info("Successfully fetched students from the DB"))
        .onErrorMap(
            TimeoutException.class,
            ex -> new StudentServiceTimeoutException("Database timeout", ex))
        .onErrorMap(
            error -> {
              log.error("[{}] Failed to fetch students from the DB : ", requestId, error);
              return new StudentServiceFailureException("Failed to fetch students ", error);
            });
    }

    private Mono<Map<String, Long>> toCountMap(Flux<KeyCountProjection> flux) {
        return flux.collectMap(
                kc  -> kc.key().toUpperCase(),
                KeyCountProjection::count,
                HashMap::new
        );
    }

    private Mono<SchoolDetailsResponse> getSchoolDetails(String schoolCode) {
        return schoolService.getSchoolDetails(schoolCode);
    }

    private Mono<Void> saveAssociatedData(Student student, CreateStudentRequest request, String schoolName) {
        return Mono.when(saveGuardians(student.getStudentId(), request.guardians()),
                saveMedicalRecord(student.getStudentId(), request.medicalInfo()),
                saveEnrollmentHistory(student, schoolName));
    }

    private Mono<Void> saveGuardians(Long studentId, List<GuardianRequest> guardianRequests) {

        if (guardianRequests == null || guardianRequests.isEmpty()) return  Mono.empty();
        return Flux.fromIterable(guardianRequests)
                .flatMap(request -> guardianService.addGuardian(studentId, request))
                .then();
    }

    private Mono<Void> saveMedicalRecord(Long studentId, MedicalRecordRequest medicalRequest) {

        if (medicalRequest == null) return Mono.empty();
        return medicalService.createMedicalRecord(studentId, medicalRequest)
                .then();
    }

    private Mono<Void> saveEnrollmentHistory(Student student, String schoolName) {

        return enrollmentService.createInitialEnrollment(student, schoolName)
                .then();
    }

    private Mono<SchoolDetailsResponse> validateSchoolCapacity(SchoolDetailsResponse response) {
        if(!response.status().equals(SchoolStatus.ACTIVE)) {
            return Mono.error(new SchoolServiceUnavailableException("School state is not ACTIVE"," school not on session"));
        }
        Long schoolId = response.schoolId();
        return studentRepository.countBySchoolId(schoolId)
                .flatMap(count -> {
                    if (count >= response.schoolCapacity()) {
                        return Mono.error(new SchoolCapacityExceededException(
                                schoolId,
                                count,
                                response.schoolCapacity()));
                    }
                    return Mono.just(response);
                });

    }
}
