package com.project.oms.order.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.oms.common.events.DomainEvent;

import java.util.UUID;

public class OrderCancelledEvent extends DomainEvent {

    private final UUID orderId;
    private final String reason;

    @JsonCreator
    public OrderCancelledEvent(@JsonProperty("orderId") UUID orderId, @JsonProperty("reason") String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getReason() {
        return reason;
    }
}
