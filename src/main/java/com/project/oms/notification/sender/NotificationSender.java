package com.project.oms.notification.sender;

import com.project.oms.notification.domain.NotificationEntity;

public interface NotificationSender {
    void send(NotificationEntity notification);
}
