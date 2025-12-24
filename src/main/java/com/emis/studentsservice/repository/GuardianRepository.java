package com.emis.studentsservice.repository;

import com.emis.studentsservice.domain.db.Guardian;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GuardianRepository extends R2dbcRepository<Guardian,Long> {


    @Query("SELECT * FROM guardians WHERE student_id = :studentId ORDER BY is_primary_contact DESC, created_at")
    Flux<Guardian> findByStudentId(Long studentId);



    @Query("DELETE FROM guardians WHERE student_id = :studentId")
    Mono<Void> deleteByStudentId(Long studentId);

    @Query("SELECT * FROM guardians WHERE student_id = :studentId AND is_primary_contact = true")
    Mono<Guardian> findPrimaryGuardianByStudentId(Long studentId);

    @Query("SELECT COUNT(*) FROM guardians WHERE student_id = :studentId")
    Mono<Long> countByStudentId(Long studentId);
}
