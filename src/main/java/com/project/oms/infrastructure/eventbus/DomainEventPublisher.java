package com.project.oms.infrastructure.eventbus;

import com.project.oms.common.events.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
