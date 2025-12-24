package com.emis.studentsservice.repository;

import com.emis.studentsservice.domain.db.MedicalRecord;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MedicalRecordRepository extends R2dbcRepository<MedicalRecord,Long> {

    @Query("SELECT * FROM medical_records WHERE student_id = :studentId")
    Mono<MedicalRecord> getStudentMedicalRecord(Long studentId);


    @Query("SELECT EXISTS(SELECT 1 FROM medical_records WHERE student_id = :studentId)")
    Mono<Boolean> existsByStudentId(Long studentId);

    @Query("DELETE FROM medical_records WHERE student_id = :studentId")
    Mono<Void> deleteByStudentId(Long studentId);

    @Query("SELECT * FROM medical_records WHERE blood_type = :bloodType")
    Flux<MedicalRecord> findByBloodType(String bloodType);
}
