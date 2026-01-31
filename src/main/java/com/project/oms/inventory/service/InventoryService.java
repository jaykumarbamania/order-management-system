package com.project.oms.inventory.service;

import com.project.oms.infrastructure.eventbus.DomainEventPublisher;
import com.project.oms.inventory.domain.InventoryItem;
import com.project.oms.inventory.events.InventoryReservedEvent;
import com.project.oms.inventory.repository.InventoryRepository;
import com.project.oms.order.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final DomainEventPublisher eventPublisher;

        @TransactionalEventListener
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void handleOrderCreated(OrderCreatedEvent event) {

            UUID productId = UUID.fromString("440e8400-e29b-41d4-a716-446655440000");

            InventoryItem item = inventoryRepository
                    .findByProductId(productId)
                    .orElseGet(() ->
                            inventoryRepository.save(
                                    new InventoryItem(productId, 100)
                            )
                    );

            item.reserve(10);

            inventoryRepository.save(item);

            eventPublisher.publish(
                    new InventoryReservedEvent(event.getOrderId())
            );
        }

}
