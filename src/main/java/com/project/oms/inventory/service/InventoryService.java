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

    private static int initialQuantity= 100;

//    @Transactional
//    @EventListener
//    public void handleOrderCreated(OrderCreatedEvent event) {
//        log.info("Inventory received OrderCreatedEvent for orderId={}", event.getOrderId());
//
//        log.info("TX active = {}", TransactionSynchronizationManager.isActualTransactionActive());
//        log.info("TX readOnly = {}", TransactionSynchronizationManager.isCurrentTransactionReadOnly());
//
//        try {
//            // TEMP SIMPLIFICATION:
//            // Assume 1 product with quantity = 1
//            InventoryItem item =
//                    inventoryRepository.findByAvailableQuantity(initialQuantity)
//                            .orElseThrow();
//
//            log.info("Inventory {}, found with available quantity {}",item.getId(),item.getAvailableQuantity());
//
//            item.reserve(10);
//
//            log.info("After reserving 10 quantities : {}",item.getAvailableQuantity());
//            InventoryItem savedItem = inventoryRepository.saveAndFlush(item);
//            log.info("Storing in DB with available quantities : {}",savedItem.getAvailableQuantity());
//            log.info("Inventory reserved successfully for orderId={}", event.getOrderId());
//
//            eventPublisher.publish(
//                    new InventoryReservedEvent(event.getOrderId())
//            );
//            log.info("Message Sent InventoryReservedEvent ");
//
//        } catch (Exception ex) {
//            log.error("Inventory reservation failed for orderId={}, reason={}",
//                    event.getOrderId(), ex.getMessage());
//
//            eventPublisher.publish(
//                    new InventoryFailedEvent(event.getOrderId(), ex.getMessage())
//            );
//        }
//    }

//    @EventListener
//    @Transactional
//    public void handleOrderCreated(OrderCreatedEvent event) {
//
//        UUID productId = UUID.fromString("440e8400-e29b-41d4-a716-446655440000");
//
//        InventoryItem item = inventoryRepository
//                .findByProductId(productId)
//                .orElseGet(() -> {
//                    InventoryItem created = new InventoryItem(productId, 100);
//                    return inventoryRepository.saveAndFlush(created);
//                });
//
//        log.info("Before reserve = {}", item.getAvailableQuantity());
//
//        item.reserve(10);
//
//        log.info("After reserve = {}", item.getAvailableQuantity());
//
//        inventoryRepository.save(item);
//
//        eventPublisher.publish(
//                new InventoryReservedEvent(event.getOrderId())
//        );
//    }


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

            log.info("Before reserve = {}", item.getAvailableQuantity());

            item.reserve(10);

            inventoryRepository.save(item); // ✅ now inside TX

            log.info("After reserve = {}", item.getAvailableQuantity());

            eventPublisher.publish(
                    new InventoryReservedEvent(event.getOrderId())
            );
        }

}
