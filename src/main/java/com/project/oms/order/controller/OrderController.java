package com.project.oms.order.controller;

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

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody CreateOrderRequest request
    ) {
        log.info("Received create order request for userId={}", request.userId());
        Order order = orderService.
                createOrder(request.userId(),request.totalAmount());

        log.info("Order created successfully with orderId={}", order.getId());

        return ResponseEntity
                .accepted()
                .body(mapToResponse(order));
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable UUID orderId) {
        log.info("Fetching order with orderId={}", orderId);

        Order order = orderService.getOrder(orderId);
        return mapToResponse(order);
    }

    @GetMapping
    public List<OrderResponse> getOrdersByUser(@RequestParam UUID userId) {
        log.info("Fetching orders for userId={}", userId);

        return orderService.getOrdersForUser(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus()
        );
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

}
