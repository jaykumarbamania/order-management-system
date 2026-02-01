package com.project.oms.inventory.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.oms.common.events.DomainEvent;

import java.util.UUID;

public class InventoryReservedEvent extends DomainEvent {

    private final UUID orderId;

    @JsonCreator
    public InventoryReservedEvent(@JsonProperty("orderId") UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
