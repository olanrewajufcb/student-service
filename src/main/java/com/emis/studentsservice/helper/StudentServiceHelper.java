package com.emis.studentsservice.helper;

import com.emis.studentsservice.domain.db.Student;
import com.emis.studentsservice.dto.request.UpdateStudentRequest;
import com.emis.studentsservice.exception.StudentNotFoundException;
import com.emis.studentsservice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class StudentServiceHelper {
    private final StudentRepository studentRepository;
    public Mono<Void> validateStudentExists(Long studentId) {
        return studentRepository.existsById(studentId)
                .flatMap(exists -> Boolean.TRUE.equals(exists) ? Mono.empty() :
                        Mono.error(new StudentNotFoundException("student does not exist")));
    }


    public Student updateStudent(Student student, UpdateStudentRequest request) {

    if (request.firstName() != null) {
            student.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            student.setLastName(request.lastName());
        }
        if (request.dateOfBirth() != null) {
            student.setDateOfBirth(request.dateOfBirth());
        }
        if (request.gender() != null) {
            student.setGender(request.gender());
        }
        if (request.enrollmentDate() != null) {
            student.setEnrollmentDate(request.enrollmentDate());
        }
        if (request.gradeLevel() != null) {
            student.setGradeLevel(request.gradeLevel().name());
        }
        if (request.status() != null) {
            student.setStatus(request.status().name());
        }
        if (request.address1() != null) {
            student.setAddress1(request.address1());
        }
        if (request.address2() != null) {
            student.setAddress2(request.address2());
        }

        if (request.phone() != null) {
            student.setPhone(request.phone());
        }
        if (request.email() != null) {
            student.setEmail(request.email());
        }
        if (request.lga() != null) {
      student.setLga(request.lga());
    }
        if (request.city() != null) {
            student.setCity(request.city());
        }
        if (request.state() != null) {
            student.setState(request.state());
        }
        if(request.postalCode() != null){
            student.setPostalCode(request.postalCode());
        }
        if (request.photoUrl() != null) {
            student.setPhotoUrl(request.photoUrl());
        }
        return student;

    }

}
