package com.project.oms.payment.events;

import com.project.oms.common.events.DomainEvent;

import java.util.UUID;

public class PaymentFailedEvent extends DomainEvent {

    private final UUID orderId;
    private final String reason;

    public PaymentFailedEvent(UUID orderId, String reason) {
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
