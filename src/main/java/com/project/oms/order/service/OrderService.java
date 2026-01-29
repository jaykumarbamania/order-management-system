package com.project.oms.order.service;

import com.project.oms.infrastructure.eventbus.DomainEventPublisher;
import com.project.oms.order.domain.Order;
import com.project.oms.order.events.OrderCreatedEvent;
import com.project.oms.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    public Order createOrder(UUID userId, BigDecimal totalAmount) {
        Order order = new Order(userId, totalAmount);
        Order saveOrder = orderRepository.save(order);

        eventPublisher.publish(
                new OrderCreatedEvent(
                        saveOrder.getId(),
                        saveOrder.getUserId(),
                        saveOrder.getTotalAmount()
                )
        );
        return saveOrder;
    }
}