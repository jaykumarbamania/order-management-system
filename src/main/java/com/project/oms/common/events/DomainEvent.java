package com.project.oms.common.events;

import java.time.Instant;

public abstract class DomainEvent {

    private final Instant occurredAt = Instant.now();

    public Instant occurredAt() {
        return occurredAt;
    }
}