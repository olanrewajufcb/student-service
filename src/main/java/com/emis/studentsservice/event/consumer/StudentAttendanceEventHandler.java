package com.emis.studentsservice.event.consumer;


import com.emis.studentsservice.event.AttendanceEvent;
import com.emis.studentsservice.event.DomainEvent;
import com.emis.studentsservice.repository.ConsumedEvent;
import com.emis.studentsservice.repository.ConsumedEventRepository;
import com.emis.studentsservice.repository.StudentAttendanceProjectionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@RequiredArgsConstructor
public class StudentAttendanceEventHandler {

    private final ConsumedEventRepository consumedEventRepository;
    private final ObjectMapper objectMapper;
    private final StudentAttendanceProjectionRepository studentAttendanceProjectionRepository;
    private final TransactionalOperator transactionalOperator;
    public Mono<Void> handle(DomainEvent<JsonNode> event) {

        if (!"ATTENDANCE_EVENT".equals(event.getEventType())){
            return Mono.empty();
        }
        return consumedEventRepository.existsById(event.getEventId())
                .flatMap(alreadyProcessed -> {
                    if (Boolean.TRUE.equals(alreadyProcessed)) {
                        log.info("Event {} already processed, skipping", event.getEventId());
                        return Mono.empty();
                    }

                    return processAttendanceEvent(event)
                            .as(transactionalOperator::transactional);
                });
    }
    private Mono<Void> processAttendanceEvent(DomainEvent<JsonNode> event) {

        AttendanceEvent payload =
                objectMapper.convertValue(
                        event.getData(),
                        AttendanceEvent.class
                );
                            return Mono.defer(() -> studentAttendanceProjectionRepository.save(payload)
                                    .then(
                                            consumedEventRepository.save(ConsumedEvent.builder()
                                                    .eventId(event.getEventId())
                                                    .eventType(event.getEventType())
                                                    .build())))
                                    .then()
                .onErrorResume(
                        ex -> {
                            log.error("Failed processing staff transfer event {}", event.getEventId(), ex);
                            return Mono.empty(); // do NOT poison Kafka
                        });
    }

}
