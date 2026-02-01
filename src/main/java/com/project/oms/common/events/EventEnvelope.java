package com.project.oms.common.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.project.oms.common.utils.JsonUtils;
import com.project.oms.common.vo.AggregateType;

import java.time.Instant;
import java.util.UUID;

public class EventEnvelope<T extends DomainEvent> {

    private final UUID eventId;
    private final AggregateType aggregateType;
    private final UUID aggregateId;
    private final String eventType;
    private final Instant occurredAt;
    private final JsonNode  payload;

    @JsonCreator
    public EventEnvelope(
            @JsonProperty("eventId") UUID eventId,
            @JsonProperty("aggregateType") AggregateType aggregateType,
            @JsonProperty("aggregateId") UUID aggregateId,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("occurredAt") Instant occurredAt,
            @JsonProperty("payload") JsonNode payload
    ) {
        this.eventId = eventId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.occurredAt = occurredAt;
        this.payload = payload;
    }

    public static <T extends DomainEvent> EventEnvelope<T> of(
            AggregateType aggregateType,
            UUID aggregateId,
            T payload
    ) {
        return new EventEnvelope<>(
                UUID.randomUUID(),
                aggregateType,
                aggregateId,
                payload.getClass().getName(),
                Instant.now(),
                JsonUtils.toTree(payload)
        );
    }

    public UUID getEventId() {
        return eventId;
    }

    public AggregateType getAggregateType() {
        return aggregateType;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public JsonNode getPayload() {
        return payload;
    }
}
