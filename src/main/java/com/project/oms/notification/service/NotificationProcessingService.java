package com.project.oms.notification.service;

import com.project.oms.notification.domain.NotificationEntity;
import com.project.oms.notification.domain.NotificationStatus;
import com.project.oms.notification.repository.NotificationRepository;
import com.project.oms.notification.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProcessingService {

    private static final int MAX_RETRIES = 3;

    private final NotificationRepository repository;
    private final NotificationSender sender;

    @Transactional
    public void process(NotificationEntity notification) {
        try {
            notification.setStatus(NotificationStatus.PROCESSING);
            notification.setUpdatedAt(Instant.now());
            repository.save(notification);

            sender.send(notification);

            notification.setStatus(NotificationStatus.SENT);
            notification.setUpdatedAt(Instant.now());
            repository.save(notification);
        } catch (Exception ex) {
            log.error("Notification Failed for id = [{}]",notification.getId(), ex);

            notification.setRetryCount((notification.getRetryCount()) + 1);

            if (notification.getRetryCount() > MAX_RETRIES ) {
                notification.setStatus(NotificationStatus.FAILED);
            } else {
                notification.setStatus(NotificationStatus.PENDING);
            }

            notification.setUpdatedAt(Instant.now());

            repository.save(notification);
        }
    }
}
