package com.project.oms.outbox.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    private UUID eventId;

    private String aggregateType;
    private UUID aggregateId;
    private String eventType;

    @Lob
    private String payload;

    private boolean published;
    private Instant occurredAt;

    protected OutboxEvent() {}

    public OutboxEvent(
            UUID eventId,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload,
            Instant occurredAt
    ) {
        this.eventId = eventId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.published = false;
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void markPublished() {
        this.published = true;
    }

    public boolean isPublished() {
        return published;
    }
}
