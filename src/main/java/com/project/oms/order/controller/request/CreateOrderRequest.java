package com.project.oms.order.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderRequest(

        @NotNull
        UUID userId,

        @NotNull
        @Positive
        BigDecimal totalAmount
) {}
