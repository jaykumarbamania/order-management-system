package com.project.oms.order.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.oms.common.events.DomainEvent;

import java.util.UUID;

public class OrderConfirmedEvent extends DomainEvent {

    private final UUID orderId;

    @JsonCreator
    public OrderConfirmedEvent(@JsonProperty("orderId") UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
