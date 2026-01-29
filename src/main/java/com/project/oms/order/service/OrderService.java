package com.project.oms.order.service;

import com.project.oms.infrastructure.eventbus.DomainEventPublisher;
import com.project.oms.inventory.events.InventoryFailedEvent;
import com.project.oms.inventory.events.InventoryReservedEvent;
import com.project.oms.order.domain.Order;
import com.project.oms.order.domain.OrderStatus;
import com.project.oms.order.events.OrderCreatedEvent;
import com.project.oms.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    public Order createOrder(UUID userId, BigDecimal totalAmount) {

        log.info("Received createOrder Request with userId {} of totalAmount : {}",userId,totalAmount);
        Order order = new Order(userId, totalAmount);
        Order saveOrder = orderRepository.save(order);

        eventPublisher.publish(
                new OrderCreatedEvent(
                        saveOrder.getId(),
                        saveOrder.getUserId(),
                        saveOrder.getTotalAmount()
                )
        );
        log.info("Order Created Successfully with orderId : {}",order.getId());

        return saveOrder;
    }

    @EventListener
    @Transactional
    public void handleInventoryReserved(InventoryReservedEvent event) {
        UUID orderId = event.getOrderId();
        log.info("Order Received InventoryReservedEvent with orderId : {}",orderId);

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order Not Found"));

        existingOrder.transitionTo(OrderStatus.INVENTORY_RESERVED);

        orderRepository.save(existingOrder);

        log.info("Order status updated to INVENTORY_RESERVED for orderId={}", orderId);
    }

    @EventListener
    @Transactional
    public void handleInventoryFailed(InventoryFailedEvent event) {
        UUID orderId = event.getOrderId();
        log.warn("Order received InventoryFailedEvent for orderId={}, reason={}",
                orderId, event.getReason());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        order.transitionTo(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled due to inventory failure for orderId={}", orderId);
    }
}