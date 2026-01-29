package com.project.oms.payment.events;

import com.project.oms.common.events.DomainEvent;

import java.util.UUID;

public class PaymentSuccessEvent extends DomainEvent {

    private final UUID orderId;

    public PaymentSuccessEvent(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
