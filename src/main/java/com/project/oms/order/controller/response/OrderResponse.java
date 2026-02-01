package com.project.oms.order.controller.response;

import com.project.oms.order.domain.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Order response")
public record OrderResponse(
        UUID orderId,
        UUID userId,
        BigDecimal totalAmount,
        OrderStatus status
) {}
