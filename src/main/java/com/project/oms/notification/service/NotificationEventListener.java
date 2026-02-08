package com.project.oms.notification.service;

import com.project.oms.notification.domain.NotificationEntity;
import com.project.oms.notification.domain.NotificationStatus;
import com.project.oms.notification.domain.NotificationType;
import com.project.oms.notification.events.NotificationEvent;
import com.project.oms.notification.repository.NotificationRepository;
import com.project.oms.order.events.OrderCancelledEvent;
import com.project.oms.order.events.OrderConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

   private final NotificationRepository notificationRepository;

   @TransactionalEventListener
   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public void handleOrderConfirmed(OrderConfirmedEvent event) {

      log.info("Notification requested for OrderConfirmedEvent, orderId={}", event.getOrderId());

      NotificationEntity notification = NotificationEntity.builder()
              .eventId(UUID.randomUUID())
              .orderId(event.getOrderId())
              .userId("SYSTEM") // later from event/user context
              .type(NotificationType.EMAIL)
              .message("Your order has been confirmed successfully.")
              .status(NotificationStatus.PENDING)
              .retryCount(0)
              .createdAt(Instant.now())
              .updatedAt(Instant.now())
              .build();

      notificationRepository.save(notification);
   }

   @TransactionalEventListener
   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public void handleOrderCancelled(OrderCancelledEvent event) {

      log.info("Notification requested for OrderCancelledEvent, orderId={}", event.getOrderId());

      NotificationEntity notification = NotificationEntity.builder()
              .eventId(UUID.randomUUID())
              .orderId(event.getOrderId())
              .userId("SYSTEM")
              .type(NotificationType.EMAIL)
              .message("Your order was cancelled. Reason: " + event.getReason())
              .status(NotificationStatus.PENDING)
              .retryCount(0)
              .createdAt(Instant.now())
              .updatedAt(Instant.now())
              .build();

      notificationRepository.save(notification);
   }
}
