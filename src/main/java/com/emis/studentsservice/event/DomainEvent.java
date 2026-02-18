package com.emis.studentsservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DomainEvent<T> {

    private String eventId;
    private String eventType;
    private int eventVersion;
    private Instant occurredAt;
    private String producer;
    private String correlationId;
    private T data;
}