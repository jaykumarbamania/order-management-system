package com.project.oms.notification.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private UUID eventId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private String status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected NotificationEntity() {}

    public NotificationEntity(
            UUID eventId,
            UUID orderId,
            String userId,
            String type,
            String message,
            String status
    ) {
        this.eventId = eventId;
        this.orderId = orderId;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.status = status;
        this.retryCount = 0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void markSent() {
        this.status = "SENT";
        this.updatedAt = Instant.now();
    }

    public void markFailed() {
        this.status = "FAILED";
        this.retryCount++;
        this.updatedAt = Instant.now();
    }
}
