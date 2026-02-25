package com.emis.studentsservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DomainEvent<T> {

    private UUID eventId;
    private String eventType;
    private int eventVersion;
    private Instant occurredAt;
    private String producer;
    private UUID correlationId;
    private T data;
}