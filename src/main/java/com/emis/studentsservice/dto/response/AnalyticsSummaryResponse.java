package com.emis.studentsservice.dto.response;

import java.util.List;

public record AnalyticsSummaryResponse(
        String schoolCode,
        String academicYear,
        Long totalStudents,
        Long maleStudents,
        Long femaleStudents,
        Long newAdmissions,
        Long transferIns,
        Long promotions,
        Long dropouts,
        Long repeaters,
        Long graduated,
        Long reEnrollments,
        List<SchoolClasses> classes,
        Long highRiskStudents,
        Long lowRiskStudents,
        Long mediumRiskStudents,
        List<StudentDetails> students
) {}
