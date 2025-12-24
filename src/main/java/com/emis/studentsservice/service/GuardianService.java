package com.emis.studentsservice.service;

import com.emis.studentsservice.dto.request.GuardianRequest;
import com.emis.studentsservice.dto.response.GuardianResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GuardianService {
    Mono<GuardianResponse> addGuardian(Long studentId, GuardianRequest request);
    Flux<GuardianResponse> getStudentGuardians(Long studentId);
    Mono<GuardianResponse> updateGuardian(Long guardianId, GuardianRequest request);
    Mono<Void> deleteGuardian(Long guardianId);
}
