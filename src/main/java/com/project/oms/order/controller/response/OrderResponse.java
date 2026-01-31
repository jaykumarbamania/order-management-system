package com.project.oms.order.controller.response;

import com.project.oms.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID userId,
        BigDecimal totalAmount,
        OrderStatus status
) {}
