package com.project.oms.order.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Create Order Request")
public record CreateOrderRequest(

        @NotNull
        UUID userId,

        @NotNull
        @Positive
        BigDecimal totalAmount
) {}
