package com.emis.studentsservice.event.handler;

import com.emis.studentsservice.event.DomainEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;


@Configuration
@Slf4j
public class StudentEventConsumer {

    private final StudentEventRouter router;
    private final ObjectMapper objectMapper;

    public StudentEventConsumer(
                                StudentEventRouter studentEventRouter,
                                ObjectMapper objectMapper) {
        this.router = studentEventRouter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public Consumer<Flux<Message<String>>> studentReportEvents() {
        return flux -> flux.concatMap(message -> {
            try{
                String payload = message.getPayload();
                DomainEvent<JsonNode> event =
                        objectMapper.readValue(payload, new TypeReference<>() {});
                log.info("Received student report event data :::: {}", event.getData());
                return router.route(event)
                        .doOnSubscribe(s -> log.info("Subscribed to the Mono returned from router.route for eventId: {}", event.getEventId()))
                        .doOnTerminate(() -> log.info("Finished execution of the Mono from router.route for eventId: {}", event.getEventId()))
                        .onErrorResume(e -> {
                            log.error("Error processing eventId: {}", event.getEventId(), e);
                            return Mono.empty();
                        });
            }
            catch (Exception ex){
                log.error("Error processing student report event :::", ex);
                return Mono.empty();
            }

        }).subscribe(null, ex -> log.error("Error processing student report event :::", ex));

    }

    @Bean
    public Consumer<Flux<Message<String>>> attendanceEvents() {
        return flux -> flux.concatMap(message -> {
            try{
                String payload = message.getPayload();
                DomainEvent<JsonNode> event =
                        objectMapper.readValue(payload, new TypeReference<>() {});
                log.info("Received student attendance event data :::: {}", event.getData());
                return router.route(event)
                        .doOnSubscribe(s -> log.info("Subscribed to the Mono returned from router.route for eventId: {}", event.getEventId()))
                        .doOnTerminate(() -> log.info("Finished execution of the Mono from router.route for eventId: {}", event.getEventId()))
                        .onErrorResume(e -> {
                            log.error("Error processing eventId: {}", event.getEventId(), e);
                            return Mono.empty();
                        });
            }
            catch (Exception ex){
                log.error("Error processing student event :::", ex);
                return Mono.empty();
            }
        }).subscribe(null, ex -> log.error("Error processing student event :::", ex));

    }
}
