package com.project.oms.infrastructure.eventbus;

import com.project.oms.common.events.EventEnvelope;

public interface DomainEventPublisher {
    void publish(EventEnvelope<?> envelope);
}