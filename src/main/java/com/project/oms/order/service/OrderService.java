package com.project.oms.order.service;

import com.project.oms.order.domain.Order;
import com.project.oms.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(UUID userId, BigDecimal totalAmount) {
        Order order = new Order(userId, totalAmount);
        return orderRepository.save(order);
    }
}