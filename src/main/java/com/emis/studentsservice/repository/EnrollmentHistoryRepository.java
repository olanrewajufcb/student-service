package com.emis.studentsservice.repository;

import com.emis.studentsservice.domain.db.EnrollmentHistory;
import com.emis.studentsservice.enums.EnrollmentType;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EnrollmentHistoryRepository extends R2dbcRepository<EnrollmentHistory, Long> {
    
    @Query("SELECT * FROM enrollment_history WHERE student_id = :studentId ORDER BY effective_date DESC")
    Flux<EnrollmentHistory> findByStudentId(Long studentId);
    
    @Query("""
        SELECT * FROM enrollment_history 
        WHERE student_id = :studentId 
        AND effective_date <= :date 
        ORDER BY effective_date DESC 
        LIMIT 1
    """)
    Mono<EnrollmentHistory> findCurrentEnrollment(Long studentId, LocalDate date);
    
    @Query("SELECT * FROM enrollment_history WHERE school_year = :schoolYear AND grade_level = :gradeLevel")
    Flux<EnrollmentHistory> findBySchoolYearAndGradeLevel(String schoolYear, String gradeLevel);
    
    @Query("SELECT * FROM enrollment_history WHERE type = :type ORDER BY effective_date DESC")
    Flux<EnrollmentHistory> findByType(EnrollmentType type, Pageable pageable);
    
    @Query("SELECT DISTINCT school_year FROM enrollment_history ORDER BY school_year DESC")
    Flux<String> findDistinctSchoolYears();
    
    @Query("DELETE FROM enrollment_history WHERE student_id = :studentId")
    Mono<Void> deleteByStudentId(Long studentId);
}