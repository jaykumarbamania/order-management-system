package com.project.oms.order.service;

import com.project.oms.common.exceptions.ResourceNotFoundException;
import com.project.oms.infrastructure.eventbus.DomainEventPublisher;
import com.project.oms.inventory.events.InventoryFailedEvent;
import com.project.oms.inventory.events.InventoryReservedEvent;
import com.project.oms.order.domain.Order;
import com.project.oms.order.domain.OrderStatus;
import com.project.oms.order.events.OrderCancelledEvent;
import com.project.oms.order.events.OrderConfirmedEvent;
import com.project.oms.order.events.OrderCreatedEvent;
import com.project.oms.order.repository.OrderRepository;
import com.project.oms.payment.events.PaymentFailedEvent;
import com.project.oms.payment.events.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.List;
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

    public List<Order> getOrdersForUser(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleInventoryReserved(InventoryReservedEvent event) {
        UUID orderId = event.getOrderId();
        log.info("Order Received InventoryReservedEvent with orderId : {}",orderId);

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));

        existingOrder.transitionTo(OrderStatus.INVENTORY_RESERVED);

        orderRepository.save(existingOrder);

        log.info("Order status updated to INVENTORY_RESERVED for orderId={}", orderId);
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleInventoryFailed(InventoryFailedEvent event) {
        UUID orderId = event.getOrderId();
        log.warn("Order received InventoryFailedEvent for orderId={}, reason={}",
                orderId, event.getReason());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.transitionTo(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled due to inventory failure for orderId={}", orderId);
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        UUID orderId = event.getOrderId();
        log.info("Order received PaymentSuccessEvent for orderId={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not found"));

        order.transitionTo(OrderStatus.PAYMENT_SUCCESS);
        order.transitionTo(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        log.info("Order confirmed for orderId={}", orderId);

        eventPublisher.publish(new OrderConfirmedEvent(orderId));
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentFailure(PaymentFailedEvent event) {
        UUID orderId = event.getOrderId();
        log.info("Order received PaymentFailedEvent for orderId={}, reason : {}", orderId, event.getReason());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not found"));

        order.transitionTo(OrderStatus.CANCELLED);

        orderRepository.save(order);

        log.info("Order cancelled due to payment failure for orderId={}", orderId);

        eventPublisher.publish(new OrderCancelledEvent(orderId, event.getReason()));
    }

    @Transactional
    public void cancelOrder(UUID orderId, String reason) {
        log.info("Cancel order requested for orderId={}, reason={}", orderId, reason);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.transitionTo(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled successfully for orderId={}", orderId);

        eventPublisher.publish(
                new OrderCancelledEvent(orderId, reason)
        );
    }

}