package com.project.oms.order.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cancel Order Request")
public record CancelOrderRequest(
        String reason
) {}
