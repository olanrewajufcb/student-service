package com.emis.studentsservice.repository;

import com.emis.studentsservice.domain.db.StudentEnrollment;
import com.emis.studentsservice.dto.response.AnalyticsSummaryResponse;
import com.emis.studentsservice.dto.response.StudentDropoutRiskResponse;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface StudentEnrollmentRepository extends ReactiveCrudRepository<StudentEnrollment, Long> {
    Flux<StudentEnrollment> findAllByStudentId(Long studentId);

    @Query(""" 
        SELECT * FROM student_enrollments
                 WHERE student_number = $1 
                   AND school_code = $2 
                   AND academic_year = $3
        AND enrollment_status = 'ACTIVE'
        """)
    Mono<StudentEnrollment> findActiveEnrollment(
            String studentNumber, String schoolCode, String academicYear);

    @Query(""" 
        SELECT * FROM student_enrollments
                 WHERE student_id = $1 
                   AND school_id = $2 
                   AND academic_year = $3
        AND enrollment_status = 'ACTIVE'
        """)
    Mono<StudentEnrollment> findByStudentIdAndSchoolIdAndAcademicYear(
            Long studentId, Long schoolId, String academicYear);

    @Query("""
SELECT
    school_id AS schoolId,
    school_code AS schoolCode,
    academic_year AS academicYear,
    total_students AS totalStudents,
    new_admissions AS newAdmissions,
    transfer_ins AS transferIns,
    re_enrollments AS reEnrollments,
    dropouts AS dropouts
FROM student_schema.student_analytics_enrollment_summary
WHERE school_code = $1 AND academic_year = $2
""")
    Mono<AnalyticsSummaryResponse> findEnrollmentSummaryBySchool(
            String schoolCode,
            String academicYear
    );

    @Query("""
SELECT
    school_id AS schoolId,
    school_code AS schoolCode,
    academic_year AS academicYear,
    total_students AS totalStudents,
    new_admissions AS newAdmissions,
    transfer_ins AS transferIns,
    re_enrollments AS reEnrollments,
    dropouts AS dropouts
FROM student_schema.student_analytics_enrollment_summary
WHERE academic_year = $1
""")
    Flux<AnalyticsSummaryResponse> findEnrollmentSummaryByAcademicYear(
            String academicYear
    );

    @Query("REFRESH MATERIALIZED VIEW CONCURRENTLY student_schema.student_analytics_enrollment_summary")
    Mono<Void> refreshEnrollmentAnalytics();

    @Query("""
SELECT
    student_id AS studentId,
    student_number AS studentNumber,
    school_code AS schoolCode,
    academic_year AS academicYear,
    attendance_rate AS attendanceRate,
    absent_days AS absentDays,
    risk_score AS riskScore,
    risk_level AS riskLevel
FROM student_schema.student_dropout_risk_level
WHERE school_code = $1
ORDER BY risk_score DESC
""")
    Flux<StudentDropoutRiskResponse> findDropoutRiskBySchool(String schoolCode);


    @Query("REFRESH MATERIALIZED VIEW CONCURRENTLY student_schema.mv_student_dropout_risk")
    Mono<Void> refreshDropoutRiskView();

    @Query("""
    SELECT se.*
    FROM student_schema.student_enrollments se
    WHERE se.school_code = :schoolCode 
      AND se.academic_year = :academicYear 
      AND se.enrollment_status = 'ACTIVE'
    ORDER BY se.created_at DESC  -- Add ordering for consistent pagination
    LIMIT :size OFFSET :offset
    """)
    Flux<StudentEnrollment> findAllBySchoolCodeAndAcademicYearAndEnrollmentStatus(
            String schoolCode, String academicYear, int size, long offset);

    @Query("""
    SELECT COUNT(*)
    FROM student_schema.student_enrollments se
    WHERE se.school_code = :schoolCode 
      AND se.academic_year = :academicYear 
      AND se.enrollment_status = 'ACTIVE'
    """)
    Mono<Long> countAllBySchoolCodeAndAcademicYearAndEnrollmentStatus(
            String schoolCode, String academicYear);
}
