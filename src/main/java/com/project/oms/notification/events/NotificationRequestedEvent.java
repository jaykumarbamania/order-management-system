package com.project.oms.notification.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.oms.common.events.DomainEvent;

import java.util.UUID;

public class NotificationRequestedEvent extends DomainEvent {

    private final UUID eventId;
    private final UUID orderId;
    private final String userId;
    private final String type;
    private final String message;

    @JsonCreator
    public NotificationRequestedEvent(@JsonProperty("eventId") UUID eventId,
                                      @JsonProperty("orderId") UUID orderId,
                                      @JsonProperty("userId") String userId,
                                      @JsonProperty("type") String type,
                                      @JsonProperty("message") String message
                                      ) {
        this.eventId = eventId;
        this.orderId = orderId;
        this.userId = userId;
        this.type = type;
        this.message = message;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    public UUID getEventId() {
        return eventId;
    }
}
