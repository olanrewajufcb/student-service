package com.emis.studentsservice.service.impl;

import com.emis.studentsservice.domain.db.Guardian;
import com.emis.studentsservice.dto.request.GuardianRequest;
import com.emis.studentsservice.dto.response.GuardianResponse;
import com.emis.studentsservice.helper.StudentServiceHelper;
import com.emis.studentsservice.mapper.GuardianMapper;
import com.emis.studentsservice.repository.GuardianRepository;
import com.emis.studentsservice.service.GuardianService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class GuardianServiceImpl implements GuardianService {

    private final GuardianRepository guardianRepository;
    private final StudentServiceHelper serviceHelper;
    private final GuardianMapper mapper;


    @Override
    public Mono<GuardianResponse> addGuardian(Long studentId, GuardianRequest request) {

        log.info("Adding guardian for student: {}", studentId);
                return buildGuardian(studentId, request)
                .flatMap(guardianRepository::save)
                .map(mapper::toResponse)
                .doOnSuccess(guardian -> log.info("Successfully added guardian: {}", guardian))
                .doOnError(error -> log.error("Failed to add guardian: {}", error.getMessage()));
    }

    @Override
    public Flux<GuardianResponse> getStudentGuardians(Long studentId) {
        return serviceHelper.validateStudentExists(studentId)
                .thenMany(guardianRepository.findByStudentId(studentId))
                .map(mapper::toResponse)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<GuardianResponse> updateGuardian(Long guardianId, GuardianRequest request) {
        return null;
    }

    @Override
    public Mono<Void> deleteGuardian(Long guardianId) {
        return null;
    }


    private Mono<Guardian> buildGuardian(Long studentId, GuardianRequest request) {
        return Mono.fromCallable(() -> Guardian.builder()
                .studentId(studentId)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .relationship(request.relationship())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .isPrimaryContact(request.isPrimaryContact() != null ? request.isPrimaryContact() : Boolean.FALSE)
                .build());
    }
}
