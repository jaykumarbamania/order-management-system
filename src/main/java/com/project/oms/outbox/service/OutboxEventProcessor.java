package com.project.oms.outbox.service;

import com.project.oms.common.events.EventEnvelope;
import com.project.oms.common.utils.JsonUtils;
import com.project.oms.outbox.domain.OutboxEvent;
import com.project.oms.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventProcessor {

    private final OutboxRepository repository;
    private final ApplicationEventPublisher springPublisher;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishOutboxEvents() {
        List<OutboxEvent> events =
                repository.findTop10ByPublishedFalseOrderByOccurredAt();

        for (OutboxEvent event : events) {

            try {
                EventEnvelope envelope =
                        JsonUtils.fromJson(event.getPayload(), EventEnvelope.class);

                Class<?> eventClass = Class.forName(envelope.getEventType());

                Object domainEvent =
                        JsonUtils.convert(envelope.getPayload(), eventClass);
                log.info("Domain Event: {}",domainEvent);
                springPublisher.publishEvent(domainEvent);
                event.markPublished();

                log.info("Outbox with eventId [{}] published: {}",event.getEventId(), event.getEventType());
            } catch (Exception ex) {
                log.error("Failed to publish outbox event {}", event.getEventType(), ex);
            }
        }
    }
}
