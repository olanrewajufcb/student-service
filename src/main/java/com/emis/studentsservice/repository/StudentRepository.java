package com.emis.studentsservice.repository;

import com.emis.studentsservice.domain.db.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StudentRepository extends R2dbcRepository<Student, Long> {

    @Query("""
    SELECT * FROM students 
    WHERE school_id = :schoolId 
    ORDER BY student_id LIMIT :size OFFSET :offset
    """)
    Flux<Student> findBySchoolId(Long schoolId, int size, long offset);

    @Query("SELECT * FROM students WHERE class_level = :classLevel")
    Flux<Student> findByClassLevel(String classLevel);

    @Query("""
        SELECT s.* FROM students s
        INNER JOIN schools sc ON s.school_id = sc.school_id
        WHERE sc.school_code = $1
        ORDER BY s.student_id
        LIMIT $2 OFFSET $3
        """)
    Flux<Student> findStudentsBySchoolCode(String schoolCode, int limit, long offset);


    @Query("SELECT * FROM students WHERE student_number = :studentNumber AND school_code = :schoolCode")
    Mono<Student> findByStudentNumberAndSchoolCode(String studentNumber, String schoolCode);

    @Query("SELECT * FROM students WHERE school_id = :schoolId AND status = :status")
    Flux<Student> findBySchoolIdAndStatus(Long schoolId, String status);

    @Query("SELECT * FROM students WHERE school_id = :schoolId AND class_level = :classLevel")
    Flux<Student> findBySchoolIdAndClassLevel(Long schoolId, String classLevel);

    @Query("SELECT * FROM students WHERE status = :status ORDER BY first_name, last_name")
    Flux<Student> findByStatus(String status, Pageable pageable);

    @Query("SELECT COUNT(*) FROM students WHERE school_id = :schoolId")
    Mono<Long> countBySchoolId(Long schoolId);

    @Query("SELECT * FROM students WHERE first_name ILIKE :query OR last_name ILIKE :query OR student_number ILIKE :query")
    Flux<Student> searchByNameOrNumber(String query);

    @Query("SELECT EXISTS(SELECT 1 FROM students WHERE id = :studentId)")
    Mono<Boolean> existsById(Long studentId);

    @Query("SELECT * FROM students WHERE school_id = :schoolId ORDER BY created_at DESC LIMIT :limit")
    Flux<Student> findRecentBySchoolId(Long schoolId, int limit);

    @Query("""
        SELECT COUNT(*) FROM students s 
        JOIN schools sc ON s.school_id = sc.id 
        WHERE sc.district = :lga AND sc.state = :state
    """)
    Mono<Long> countByLgaAndState(String lga, String state);


    @Query("""
        SELECT s.class_level AS classLevel, COUNT(*) AS count 
        FROM students s 
        JOIN schools sc ON s.school_id = sc.id 
        WHERE sc.lga = :lga AND sc.state = :state
        GROUP BY s.class_level
    """)
    Flux<StudentCountPerClassLevel> countByClassLevelAndLga(String lga, String state);


    // STATE-LEVEL QUERIES (new)
    @Query("""
        SELECT COUNT(*) FROM students s 
        JOIN schools sc ON s.school_id = sc.id 
        WHERE sc.state = :state
    """)
    Mono<Long> countStudentsByState(String state);

    @Query("""
        SELECT COUNT(*) FROM students s 
        JOIN schools sc ON s.school_id = sc.id 
        WHERE sc.state = :state AND s.status = 'ACTIVE'
    """)
    Mono<Long> countActiveStudentsByState(String state);

    @Query("""
        SELECT s.class_level AS classLevel, COUNT(*) as count 
        FROM students s 
        JOIN schools sc ON s.school_id = sc.id 
        WHERE sc.state = :state
        GROUP BY s.class_level
    """)
    Flux<StudentCountPerClassLevel> countStudentsByClassLevelAndState(String state);

    @Query("""
        SELECT sc.lga AS lga, 
               COUNT(DISTINCT sc.id) As totalSchools, 
               COUNT(s.id) as totalStudents,
               COUNT(CASE WHEN s.status = 'ACTIVE' THEN 1 END) as activeStudents
        FROM students s 
        JOIN schools sc ON s.school_id = sc.id 
        WHERE sc.state = :state
        GROUP BY sc.lga
        ORDER BY totalStudents DESC
    """)
    Flux<LgaStudentStatistics> getStudentCountByLgaAndState(String state);


    @Query("SELECT * FROM students WHERE student_id = ANY(:ids)") //If ANY(:ids) is not supported by Postgres R2DBC, fallback:
    Flux<Student> findAllById(List<Long> ids);

//    Flux<Student> findByStudentIdIn(Iterable<Long> ids);

}
