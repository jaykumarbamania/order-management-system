package com.project.oms.notification.events;

import com.project.oms.notification.domain.NotificationType;

import java.util.UUID;

import java.time.Instant;
import java.util.UUID;

public record NotificationEvent(
        UUID eventId,
        UUID orderId,
        String userId,
        String message,
        NotificationType type,
        Instant createdAt
) {
    public static NotificationEvent create(
            UUID orderId,
            String userId,
            String message,
            NotificationType type
    ) {
        return new NotificationEvent(
                UUID.randomUUID(),
                orderId,
                userId,
                message,
                type,
                Instant.now()
        );
    }
}
