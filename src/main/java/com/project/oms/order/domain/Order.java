package com.project.oms.order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Version
    private Long version;

    public Order(UUID userId, BigDecimal totalAmount) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.CREATED;
    }

    public void transitionTo(OrderStatus nextStatus) {
        if(!OrderStateMachine.isValidTransition(this.status,nextStatus)) {
            throw new IllegalStateException(
                    "Invalid order state transition: " + status + " -> " + nextStatus
            );
        }
        this.status = nextStatus;
    }
}