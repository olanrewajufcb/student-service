package com.emis.studentsservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ConsumedEventRepository
        extends ReactiveCrudRepository<ConsumedEvent, UUID> {
}