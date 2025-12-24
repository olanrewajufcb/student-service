package com.emis.studentsservice.service;

import com.emis.studentsservice.dto.request.MedicalRecordRequest;
import com.emis.studentsservice.dto.response.MedicalRecordResponse;
import reactor.core.publisher.Mono;

public interface MedicalRecordService {
    Mono<MedicalRecordResponse> createMedicalRecord(Long studentId, MedicalRecordRequest request);
    Mono<MedicalRecordResponse> getMedicalRecord(Long studentId);
    Mono<MedicalRecordResponse> updateMedicalRecord(Long studentId, MedicalRecordRequest request);
}
