package com.project.oms.order.events;

import com.project.oms.common.events.DomainEvent;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderCreatedEvent extends DomainEvent {

    private final UUID orderId;
    private final UUID userId;
    private final BigDecimal totalAmount;

    public OrderCreatedEvent(UUID orderId, UUID userId, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
