package com.emis.studentsservice.event.handler;

import com.emis.studentsservice.event.DomainEvent;
import com.emis.studentsservice.event.consumer.StudentAttendanceEventHandler;
import com.emis.studentsservice.event.consumer.StudentReportProcessingService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StudentEventRouter {

    private final StudentAttendanceEventHandler studentAttendanceEventHandler;
    private final StudentReportProcessingService studentReportProcessingService;

    public Mono<Void> route(DomainEvent<JsonNode> event) {
        return switch (event.getEventType()) {
            case "ATTENDANCE_EVENT" -> studentAttendanceEventHandler.handle(event);

            case "STUDENT_REPORT_REQUESTED" -> studentReportProcessingService.process(event);

            default ->  {
                log.warn("Unhandled student event type: {}", event.getEventType());
                yield Mono.empty();
            }
        };
    }
}
