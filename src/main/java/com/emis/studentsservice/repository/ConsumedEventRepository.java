package com.emis.studentsservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ConsumedEventRepository
        extends ReactiveCrudRepository<ConsumedEvent, String> {
}