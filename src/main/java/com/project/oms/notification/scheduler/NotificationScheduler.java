package com.project.oms.notification.scheduler;


import com.project.oms.notification.domain.NotificationEntity;
import com.project.oms.notification.domain.NotificationStatus;
import com.project.oms.notification.repository.NotificationRepository;
import com.project.oms.notification.service.NotificationProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationRepository repository;
    private final NotificationProcessingService processor;

    @Scheduled(fixedDelay = 10000)
    public void processPendingNotifications() {
        List<NotificationEntity> notifications = repository.findTop10ByStatusOrderByCreatedAtAsc(NotificationStatus.PENDING);

        if(notifications.isEmpty()) return;

        log.info("Processing {} pending notifications",notifications.size());

        notifications.forEach(processor::process);
    }
}
