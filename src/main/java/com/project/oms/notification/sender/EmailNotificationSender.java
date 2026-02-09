package com.project.oms.notification.sender;

import com.project.oms.notification.domain.NotificationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationSender implements NotificationSender {

    @Override
    public void send(NotificationEntity notification) {
        log.info(
            "Sending {} notification for orderId={}, userId={}",
            notification.getType(),
            notification.getOrderId(),
            notification.getUserId()
        );

        // simulate success
        if (Math.random() > 0.2) {
            throw new RuntimeException("SMTP failure");
        }

        log.info("Email sent successfully for orderId={}", notification.getOrderId());
    }
}
