package com.emis.studentsservice.event.publisher;

import com.emis.studentsservice.domain.db.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class StudentEventPublisher {

  private final StreamBridge streamBridge;

  public Mono<Void> publish(OutboxEvent outboxEvent) {
    return Mono.fromCallable(
            () -> {
              boolean sent =
                  streamBridge.send(
                      outboxEvent.getTopic(),
                      MessageBuilder.withPayload(outboxEvent.getPayload())
                          .setHeader("eventId", outboxEvent.getEventId().toString())
                          .setHeader("aggregateId", outboxEvent.getAggregateId())
                          .setHeader("eventType", outboxEvent.getEventType())
                          .build());

              if (!sent) {
                throw new IllegalStateException("Failed to send Kafka message");
              }

              return true;
            })
        .then().subscribeOn(Schedulers.boundedElastic());
  }
}