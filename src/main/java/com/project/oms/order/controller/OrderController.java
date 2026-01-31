package com.project.oms.order.controller;

import com.project.oms.common.idempotency.IdempotencyService;
import com.project.oms.common.utils.JsonUtils;
import com.project.oms.order.controller.request.CancelOrderRequest;
import com.project.oms.order.controller.request.CreateOrderRequest;
import com.project.oms.order.controller.response.OrderResponse;
import com.project.oms.order.domain.Order;
import com.project.oms.order.domain.OrderStatus;
import com.project.oms.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final IdempotencyService idempotencyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody @Valid CreateOrderRequest request
    ) {
        log.info("Create order request received, userId={}, idemKey={}",
                request.userId(), idempotencyKey);

        if (idempotencyKey != null) {
            return handleIdempotentCreate(idempotencyKey, request);
        }

        Order order = orderService.createOrder(
                request.userId(),
                request.totalAmount()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToResponse(order));
    }



    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable UUID orderId) {
        log.info("Fetching order with orderId={}", orderId);

        Order order = orderService.getOrder(orderId);
        return mapToResponse(order);
    }

    @GetMapping()
    public List<OrderResponse> getOrdersByUser(@RequestParam UUID userId) {
        log.info("Fetching orders for userId={}", userId);

        return orderService.getOrdersForUser(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @PostMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(
            @PathVariable UUID orderId,
            @RequestBody(required = false) CancelOrderRequest request
    ) {
        log.info("Received cancel request for orderId={}", orderId);

        String reason = request != null ? request.reason() : "User requested cancellation";
        orderService.cancelOrder(orderId, reason);
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }

    private ResponseEntity<OrderResponse> handleIdempotentCreate(
            String idempotencyKey,
            CreateOrderRequest request
    ) {
        return idempotencyService.find(idempotencyKey)
                .map(record -> {
                    log.info("Idempotent hit for key={}", idempotencyKey);

                    OrderResponse response =
                            JsonUtils.fromJson(record.getResponseBody(), OrderResponse.class);

                    return ResponseEntity
                            .status(record.getStatusCode())
                            .body(response);
                })
                .orElseGet(() -> {
                    Order order = orderService.createOrder(
                            request.userId(),
                            request.totalAmount()
                    );

                    OrderResponse response = mapToResponse(order);

                    idempotencyService.save(
                            idempotencyKey,
                            JsonUtils.toJson(response),
                            HttpStatus.CREATED.value()
                    );

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(response);
                });
    }

}
