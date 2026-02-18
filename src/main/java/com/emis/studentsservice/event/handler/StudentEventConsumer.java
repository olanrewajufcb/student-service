package com.emis.studentsservice.event.handler;

import com.emis.studentsservice.event.DomainEvent;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;


@Configuration
@Slf4j
public class StudentEventConsumer {

    private final StudentEventRouter router;

    public StudentEventConsumer(StudentEventRouter studentEventRouter) {
        this.router = studentEventRouter;
    }

    @Bean
    public Function<Flux<DomainEvent<JsonNode>>, Mono<Void>> studentReportEvents() {
        return flux ->
         flux
                .flatMap(router::route)
                    .doOnError(ex -> log.error("Error processing student report event :::", ex))
                    .then();

    }

    @Bean
    public Function<Flux<DomainEvent<JsonNode>>, Mono<Void>> attendanceEvents() {
        return flux ->
         flux
                .flatMap(router::route)
                    .doOnError(ex -> log.error("Error processing student attendance event :::", ex))
                    .then();

    }
}
