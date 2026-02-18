package com.emis.studentsservice.dto.response;

import com.emis.studentsservice.domain.db.Student;
import com.emis.studentsservice.domain.db.StudentEnrollment;

import java.time.LocalDate;


public record StudentEnrollmentResponse(
        Long enrollmentId,
        String studentNumber,
        Long studentId,
        String studentName,
        String schoolCode,
        String enrollmentType,
        String enrollmentStatus,
        LocalDate enrollmentDate
) {
    public static StudentEnrollmentResponse from(StudentEnrollment studentEnrollment, Student student) {
        return new StudentEnrollmentResponse(
                studentEnrollment.getEnrollmentId(),
                studentEnrollment.getStudentNumber(),
                studentEnrollment.getStudentId(),
                student.getFirstName(),
                studentEnrollment.getSchoolCode(),
                studentEnrollment.getEnrollmentType(),
                studentEnrollment.getEnrollmentStatus(),
                studentEnrollment.getEnrollmentDate()
        );
    }

    public static StudentEnrollmentResponse from(StudentEnrollment studentEnrollment) {
        return new StudentEnrollmentResponse(
                studentEnrollment.getEnrollmentId(),
                studentEnrollment.getStudentNumber(),
                studentEnrollment.getStudentId(),
                null,
                studentEnrollment.getSchoolCode(),
                studentEnrollment.getEnrollmentType(),
                studentEnrollment.getEnrollmentStatus(),
                studentEnrollment.getEnrollmentDate()
        );
    }
}
