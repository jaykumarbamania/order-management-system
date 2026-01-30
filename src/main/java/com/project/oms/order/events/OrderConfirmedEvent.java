package com.project.oms.order.events;

import com.project.oms.common.events.DomainEvent;

import java.util.UUID;

public class OrderConfirmedEvent extends DomainEvent {

    private final UUID orderId;

    public OrderConfirmedEvent(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
