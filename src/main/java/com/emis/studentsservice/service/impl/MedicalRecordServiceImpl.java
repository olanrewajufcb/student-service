package com.emis.studentsservice.service.impl;

import com.emis.studentsservice.domain.db.MedicalRecord;
import com.emis.studentsservice.dto.request.MedicalRecordRequest;
import com.emis.studentsservice.dto.response.MedicalRecordResponse;
import com.emis.studentsservice.exception.MedicalRecordAlreadyExistsException;
import com.emis.studentsservice.exception.StudentNotFoundException;
import com.emis.studentsservice.mapper.MedicalMapper;
import com.emis.studentsservice.repository.MedicalRecordRepository;
import com.emis.studentsservice.repository.StudentRepository;
import com.emis.studentsservice.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final StudentRepository studentRepository;
    private final MedicalMapper medicalMapper;

    @Override
    public Mono<MedicalRecordResponse> createMedicalRecord(Long studentId, MedicalRecordRequest request) {
        MedicalRecord medicalRecord = medicalMapper.toEntity(request);
        medicalRecord.setStudentId(studentId);
                return checkMedicalRecordExists(studentId)
                .then(medicalRecordRepository.save(medicalRecord))
                .map(MedicalMapper.INSTANCE::toResponse)
                .doOnError(error -> log.error("Failed to add medical record: {}", error.getMessage()));

    }

    @Override
    public Mono<MedicalRecordResponse> getMedicalRecord(Long studentId) {
        return null;
    }

    @Override
    public Mono<MedicalRecordResponse> updateMedicalRecord(Long studentId, MedicalRecordRequest request) {
        return null;
    }

    private Mono<Void> validateStudentExists(Long studentId) {
        return studentRepository.existsById(studentId)
                .flatMap(exists ->
                        Boolean.TRUE.equals(exists)
                                ? Mono.empty()
                                : Mono.error(new StudentNotFoundException(studentId)));
    }

    private Mono<Void> checkMedicalRecordExists(Long studentId) {
    return medicalRecordRepository
        .existsByStudentId(studentId)
        .flatMap(
            exists ->
                Boolean.TRUE.equals(exists)
                    ? Mono.error(new MedicalRecordAlreadyExistsException(studentId))
                    : Mono.empty());
    }
}
