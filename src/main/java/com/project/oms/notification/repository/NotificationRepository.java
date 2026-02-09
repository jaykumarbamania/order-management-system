package com.project.oms.notification.repository;

import com.project.oms.notification.domain.NotificationEntity;
import com.project.oms.notification.domain.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<NotificationEntity, Long> {

    Optional<NotificationEntity> findByEventId(UUID eventId);

    List<NotificationEntity> findTop10ByStatusOrderByCreatedAtAsc(NotificationStatus status);
}
