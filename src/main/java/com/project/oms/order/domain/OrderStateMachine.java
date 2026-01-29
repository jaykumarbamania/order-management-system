package com.project.oms.order.domain;

import java.util.Map;
import java.util.Set;

public final class OrderStateMachine {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS =
            Map.of(
                    OrderStatus.CREATED,
                    Set.of(OrderStatus.INVENTORY_RESERVED,OrderStatus.CANCELLED),

                    OrderStatus.INVENTORY_RESERVED,
                    Set.of(OrderStatus.PAYMENT_SUCCESS, OrderStatus.CANCELLED),

                    OrderStatus.PAYMENT_SUCCESS,
                    Set.of(OrderStatus.CONFIRMED)
            );

    private OrderStateMachine () {} //preventing initialisation

    public static boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return ALLOWED_TRANSITIONS
                .getOrDefault(current, Set.of())
                .contains(next);
    }
}
