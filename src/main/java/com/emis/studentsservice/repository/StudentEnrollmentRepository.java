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
    school_id AS school_id,
    school_code AS school_code,
    academic_year AS academic_year,
    total_students AS total_students,
    new_admissions AS new_admissions,
    transfer_ins AS transfer_ins,
    re_enrollments AS re_enrollments,
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
    school_id AS school_id,
    school_code AS school_code,
    academic_year AS academic_year,
    total_students AS total_students,
    new_admissions AS new_admissions,
    transfer_ins AS transfer_ins,
    re_enrollments AS re_enrollments,
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
    r.student_id AS student_id,
    r.student_number AS student_number,
    r.school_code AS school_code,
    r.academic_year AS academic_year,
    r.attendance_rate AS attendance_rate,
    r.absent_days AS absent_days,
    r.risk_score AS risk_score,
    l.risk_level AS risk_level
FROM student_schema.mv_student_dropout_risk r
JOIN student_schema.student_dropout_risk_level l ON r.student_id = l.student_id AND r.academic_year = l.academic_year
WHERE r.school_code = $1
ORDER BY r.risk_score DESC
""")
    Flux<StudentDropoutRiskResponse> findDropoutRiskBySchool(String schoolCode);


    @Query("REFRESH MATERIALIZED VIEW CONCURRENTLY student_schema.mv_student_dropout_risk")
    Mono<Void> refreshDropoutRiskView();

    @Query("REFRESH MATERIALIZED VIEW CONCURRENTLY student_schema.student_dropout_risk_level")
    Mono<Void> refreshDropoutRiskLevelView();

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
