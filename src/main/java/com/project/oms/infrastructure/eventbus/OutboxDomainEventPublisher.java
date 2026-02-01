package com.project.oms.infrastructure.eventbus;

import com.project.oms.common.events.DomainEvent;
import com.project.oms.common.events.EventEnvelope;
import com.project.oms.common.utils.JsonUtils;
import com.project.oms.outbox.domain.OutboxEvent;
import com.project.oms.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary //generally do NOT want services to bypass Outbox accidentally
@Component
@RequiredArgsConstructor
public class OutboxDomainEventPublisher implements DomainEventPublisher {

    private final OutboxRepository repository;

    @Override
    public void publish(EventEnvelope<?> envelope) {
        repository.save(
                new OutboxEvent(
                        envelope.getEventId(),
                        envelope.getAggregateType().name(),
                        envelope.getAggregateId(),
                        envelope.getEventType(),
                        JsonUtils.toJson(envelope),
                        envelope.getOccurredAt()
                )
        );
    }
}

