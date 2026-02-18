package com.emis.studentsservice.dto.response;

import com.emis.studentsservice.domain.db.StudentEnrollment;

public record PromotionResponse(
        String studentNumber,
        String schoolCode,
        String academicYear,
        Long classId,
        String className
) {
    public static PromotionResponse from(StudentEnrollment enrollment) {
    return new PromotionResponse(
        enrollment.getStudentNumber(),
        enrollment.getSchoolCode(),
        enrollment.getAcademicYear(),
        enrollment.getClassId(),
        enrollment.getClassName());
    }
}
