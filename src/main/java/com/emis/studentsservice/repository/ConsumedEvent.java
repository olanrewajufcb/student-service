package com.emis.studentsservice.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(schema = "student_schema", name = "consumed_events")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumedEvent {

    @Id
    private String eventId;
    private String eventType;
    private Instant consumedAt;
}