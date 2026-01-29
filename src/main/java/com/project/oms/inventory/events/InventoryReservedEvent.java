package com.project.oms.inventory.events;

import com.project.oms.common.events.DomainEvent;

import java.util.UUID;

public class InventoryReservedEvent extends DomainEvent {

    private final UUID orderId;

    public InventoryReservedEvent(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
