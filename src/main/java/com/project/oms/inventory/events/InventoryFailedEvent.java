package com.project.oms.inventory.events;

import com.project.oms.common.events.DomainEvent;

import java.util.UUID;

public class InventoryFailedEvent extends DomainEvent {

    private final UUID orderId;
    private final String reason;

    public InventoryFailedEvent(UUID orderId, String reason) {
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
