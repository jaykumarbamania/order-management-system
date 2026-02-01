package com.project.oms.common.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Standard error response")
public record ErrorResponse(
        @Schema(example = "2026-01-31T13:15:48Z")
        Instant timestamp,

        @Schema(example = "404")
        int status,

        @Schema(example = "NOT_FOUND")
        String error,

        @Schema(example = "Order not found")
        String message,

        @Schema(example = "/api/orders/123")
        String path
) {}

