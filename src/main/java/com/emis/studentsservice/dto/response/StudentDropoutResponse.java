package com.emis.studentsservice.dto.response;

import com.emis.studentsservice.domain.db.StudentEnrollment;

import java.time.LocalDate;

public record StudentDropoutResponse(
        String studentNumber,
        String status,
        String academicYear,
        LocalDate dropoutDate
) {
    public static StudentDropoutResponse fromEntity(StudentEnrollment enrollment) {
        return new StudentDropoutResponse(
                enrollment.getStudentNumber(),
                enrollment.getEnrollmentStatus(),
                enrollment.getAcademicYear(),
                enrollment.getDropoutDate()
        );
    }
}
