package com.project.oms.inventory.service;

import com.project.oms.infrastructure.eventbus.DomainEventPublisher;
import com.project.oms.inventory.domain.InventoryItem;
import com.project.oms.inventory.events.InventoryFailedEvent;
import com.project.oms.inventory.events.InventoryReservedEvent;
import com.project.oms.inventory.repository.InventoryRepository;
import com.project.oms.order.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final DomainEventPublisher eventPublisher;

    @EventListener
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Inventory received OrderCreatedEvent for orderId={}", event.getOrderId());

        try {
            // TEMP SIMPLIFICATION:
            // Assume 1 product with quantity = 1
            InventoryItem item = inventoryRepository.findAll()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No inventory found"));

            item.reserve(1);
            inventoryRepository.save(item);

            log.info("Inventory reserved successfully for orderId={}", event.getOrderId());

            eventPublisher.publish(
                    new InventoryReservedEvent(event.getOrderId())
            );
        } catch (Exception ex) {
            log.error("Inventory reservation failed for orderId={}, reason={}",
                    event.getOrderId(), ex.getMessage());

            eventPublisher.publish(
                    new InventoryFailedEvent(event.getOrderId(), ex.getMessage())
            );
        }
    }
}
