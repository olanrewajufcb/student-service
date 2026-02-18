package com.emis.studentsservice.service.impl;

import com.emis.studentsservice.config.StudentServiceConfigurationProperties;
import com.emis.studentsservice.domain.db.Student;
import com.emis.studentsservice.domain.db.StudentEnrollment;
import com.emis.studentsservice.dto.request.PromotionRequest;
import com.emis.studentsservice.dto.request.StudentDropoutRequest;
import com.emis.studentsservice.dto.request.StudentEnrollmentRequest;
import com.emis.studentsservice.dto.request.StudentTransferRequest;
import com.emis.studentsservice.dto.response.PromotionResponse;
import com.emis.studentsservice.dto.response.SchoolDetailsResponse;
import com.emis.studentsservice.dto.response.StudentDropoutResponse;
import com.emis.studentsservice.dto.response.StudentEnrollmentResponse;
import com.emis.studentsservice.enums.EnrollmentStatus;
import com.emis.studentsservice.enums.EnrollmentType;
import com.emis.studentsservice.enums.ProgressionStatus;
import com.emis.studentsservice.exception.*;
import com.emis.studentsservice.repository.StudentEnrollmentRepository;
import com.emis.studentsservice.repository.StudentRepository;
import com.emis.studentsservice.service.StudentEnrollmentService;
import com.emis.studentsservice.service.client.SchoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final StudentRepository studentRepository;
    private final TransactionalOperator transactionalOperator;
    private final SchoolService schoolService;
    private final StudentServiceConfigurationProperties properties;
    @Override
    public Mono<StudentEnrollmentResponse> createStudentEnrollment(
            String studentNumber, StudentEnrollmentRequest request, String requestId) {
        return studentRepository.findByStudentNumber(studentNumber)
                .switchIfEmpty(Mono.error(new RuntimeException("Student not found")))
                .flatMap(student -> studentEnrollmentRepository
                            .findByStudentIdAndSchoolIdAndAcademicYear(
                                    student.getStudentId(),
                                    student.getSchoolId(),
                                    request.academicYear())
                            .map(StudentEnrollmentResponse::from)
                            .switchIfEmpty(createStudentEnrollmentEntity(request, student))
                )
                .doOnSuccess(response -> log.info("Student enrollment created with ID: {}", requestId))
                .onErrorMap(ex -> new StudentServiceException(ex.getMessage()));
    }

    @Override
    public Mono<StudentEnrollmentResponse> getStudentEnrollment(String studentNumber,
                                     String schoolCode,  String academicYear,  String requestId) {
        return studentRepository.findByStudentNumberAndSchoolCode(studentNumber, schoolCode)
                .switchIfEmpty(Mono.error(new StudentNotFoundException("Student not found")))
                .flatMap(student -> studentEnrollmentRepository
                        .findByStudentIdAndSchoolIdAndAcademicYear(student.getStudentId(), student.getSchoolId(),
                                academicYear)
                        .map(studentEnrollment -> StudentEnrollmentResponse.from(studentEnrollment, student))
                        .switchIfEmpty(Mono.error(new StudentNotFoundException("Student enrollment not found")))
                );
    }

    @Override
    public Mono<StudentEnrollmentResponse> transferStudent(String studentNumber, StudentTransferRequest request, String requestId) {
        return studentRepository.findByStudentNumber(studentNumber)
                .switchIfEmpty(Mono.error(new StudentNotFoundException("Student not found")))
                .flatMap(student -> studentEnrollmentRepository
                        .findActiveEnrollment(
                                student.getStudentNumber(),
                                request.fromSchoolCode(),
                                request.academicYear())
                        .switchIfEmpty(Mono.error(new StudentNotFoundException("Student is not actively enrolled before")))
                        .flatMap(oldEnrollment ->  {
                            if(!oldEnrollment.getAcademicYear().equals(request.academicYear())){
                                return Mono.error(new ValidationException("Academic year mismatch"));
                            }
                             return schoolService
                                            .getSchoolDetails(request.toSchoolCode())
                                            .flatMap(targetSchool ->
                                                    executeTransfer(student, oldEnrollment, targetSchool, request)
                                            );
                                }
                        )
                )
                .onErrorMap(err -> {
                    if (err instanceof SchoolNotFoundException
                            || err instanceof SchoolServiceUnavailableException
                            || err instanceof SchoolServiceException
                            || err instanceof ResourceNotFoundException) {
                        return err;
                    }
                    return new StudentServiceException(err.getMessage());
                });
    }

    @Override
    public Mono<PromotionResponse> promoteStudent(String studentNumber, PromotionRequest request, String requestId) {
        return studentRepository.findByStudentNumber(studentNumber)
                .switchIfEmpty(Mono.error(new StudentNotFoundException("Student not found")))

                .flatMap(student ->
                        studentEnrollmentRepository
                                .findByStudentIdAndSchoolIdAndAcademicYear(
                                        student.getStudentId(),
                                        student.getSchoolId(),
                                        request.fromAcademicYear()
                                )
                                .switchIfEmpty(
                                        Mono.error(new ValidationException("No active enrollment found"))
                                )
                                .flatMap(current -> {

                                    if (!current.getSchoolCode().equals(request.schoolCode())) {
                                        return Mono.error(new ValidationException("School mismatch"));
                                    }

                                    return executePromotion(student, current, request);
                                })
                );
    }

    @Override
    public Mono<StudentDropoutResponse> dropoutStudent(String studentNumber, StudentDropoutRequest request, String requestId) {
        return studentRepository.findByStudentNumber(studentNumber)
                .switchIfEmpty(Mono.error(new StudentNotFoundException("Student not found")))

                .flatMap(student ->
                        studentEnrollmentRepository
                                .findByStudentIdAndSchoolIdAndAcademicYear(
                                        student.getStudentId(),
                                        student.getSchoolId(),
                                        request.academicYear()
                                )
                                .switchIfEmpty(
                                        Mono.error(new ValidationException("No active enrollment found"))
                                )
                                .flatMap(enrollment -> executeDropout(enrollment, request))
                );
    }

    @Override
    public Mono<Page<StudentEnrollmentResponse>> getAllStudentEnrollments(
            String schoolCode, String academicYear, Pageable pageable, String requestId) {

        int size = pageable.getPageSize();
        long offset = pageable.getOffset();

        return Mono.zip(studentEnrollmentRepository
                .findAllBySchoolCodeAndAcademicYearAndEnrollmentStatus(schoolCode, academicYear, size, offset)
                .collectList(),
                studentEnrollmentRepository
                        .countAllBySchoolCodeAndAcademicYearAndEnrollmentStatus(schoolCode, academicYear))
                .timeout(Duration.ofSeconds(properties.getTimeout()))
                .map(tuple -> {
                    List<StudentEnrollment> enrollments = tuple.getT1();
                    long total = tuple.getT2();

                    List<StudentEnrollmentResponse> responses = total == 0
                            ? List.of()
                            : enrollments.stream()
                            .map(StudentEnrollmentResponse::from)
                            .toList();
                  return (Page<StudentEnrollmentResponse>) new PageImpl<>(responses, pageable, total);

                })
                .onErrorMap(err -> {
                    if (err instanceof TimeoutException || err.getMessage().contains("timeout")) {
                        log.warn("Timeout occurred while fetching student enrollments for school: {}, academic year: {}",
                                schoolCode, academicYear);
                        return new StudentsServiceTimeoutException("Operation timed out",  err);
                    }
                    return new StudentServiceException(err.getMessage());
                });

    }

    private Mono<StudentDropoutResponse> executeDropout(
            StudentEnrollment enrollment,
            StudentDropoutRequest request
    ) {

        return Mono.defer(() -> {

            enrollment.setEnrollmentStatus(
                    EnrollmentStatus.DROPPED_OUT.name()
            );

            enrollment.setDropoutReason(request.reason());
            enrollment.setDropoutDate(LocalDate.now());
            enrollment.setCompletionDate(LocalDate.now());
            enrollment.setRemarks(request.remarks());

            return studentEnrollmentRepository.save(enrollment)
                    .as(transactionalOperator::transactional)
                    .map(StudentDropoutResponse::fromEntity);
        });
    }

    private Mono<PromotionResponse> executePromotion(
            Student student,
            StudentEnrollment current,
            PromotionRequest request
    ) {


            current.setEnrollmentStatus(EnrollmentStatus.COMPLETED.name());
            determineProgressionStatus(current, request);
            current.setCompletionDate(LocalDate.now());

            StudentEnrollment newEnrollment =
                    StudentEnrollment.builder()
                            .studentId(student.getStudentId())
                            .studentNumber(student.getStudentNumber())
                            .schoolId(current.getSchoolId())
                            .schoolCode(current.getSchoolCode())
                            .classId(request.progressionStatus() == ProgressionStatus.PROMOTED
                                    ? request.nextClassId()
                                    : request.progressionStatus() == ProgressionStatus.REPEATED
                            ?  current.getClassId() : 0)
                            .className(request.progressionStatus() == ProgressionStatus.PROMOTED
                                    ? request.className()
                                    : request.progressionStatus() == ProgressionStatus.REPEATED
                                    ?  current.getClassName() : "")
                            .academicYear(request.toAcademicYear())
                            .enrollmentStatus(EnrollmentStatus.ACTIVE.name())
                            .enrollmentType(EnrollmentType.NEW.name())
                            .enrollmentDate(LocalDate.now())
                            .build();

            return studentEnrollmentRepository.save(current)
                    .then(studentEnrollmentRepository.save(newEnrollment))
                    .as(transactionalOperator::transactional)
                    .map(PromotionResponse::from);
    }

    private void determineProgressionStatus(StudentEnrollment current, PromotionRequest request) {
        if (request.progressionStatus() == ProgressionStatus.PROMOTED){
            current.setProgressionStatus(ProgressionStatus.PROMOTED.name());

        } else if (request.progressionStatus() == ProgressionStatus.REPEATED){
            current.setProgressionStatus(ProgressionStatus.REPEATED.name());
        }
    }

    private Mono<StudentEnrollmentResponse> executeTransfer(
            Student student,
            StudentEnrollment oldEnrollment,
            SchoolDetailsResponse targetSchool,
            StudentTransferRequest request
    ) {

        return Mono.defer(() -> {

            oldEnrollment.setEnrollmentStatus(EnrollmentType.TRANSFERRED_OUT.name());
            oldEnrollment.setExitDate(LocalDate.now());
            oldEnrollment.setExitReason(EnrollmentType.TRANSFERRED_OUT.name());

            StudentEnrollment newEnrollment = StudentEnrollment.builder()
                    .studentId(student.getStudentId())
                    .studentNumber(student.getStudentNumber())
                    .schoolId(targetSchool.schoolId())
                    .schoolCode(targetSchool.schoolCode())
                    .academicYear(request.academicYear())
                    .enrollmentType(EnrollmentType.TRANSFERRED_IN.name())
                    .enrollmentStatus(EnrollmentStatus.ACTIVE.name())
                    .enrollmentDate(LocalDate.now())
                    .build();

            return studentEnrollmentRepository.save(oldEnrollment)
                    .then(studentEnrollmentRepository.save(newEnrollment))
                    .as(transactionalOperator::transactional)
                    .map(StudentEnrollmentResponse::from);
        });
    }
    private Mono<StudentEnrollmentResponse> createStudentEnrollmentEntity(
            StudentEnrollmentRequest request, Student student) {

    StudentEnrollment studentEnrollment =
        StudentEnrollment.builder()
            .studentId(student.getStudentId())
            .studentNumber(student.getStudentNumber())
            .schoolId(student.getSchoolId())
            .schoolCode(student.getSchoolCode())
            .academicYear(request.academicYear())
            .enrollmentType(request.enrollmentType().name())
            .build();
        return studentEnrollmentRepository.save(studentEnrollment)
                .as(transactionalOperator::transactional)
                .map(StudentEnrollmentResponse::from);
        }
}
