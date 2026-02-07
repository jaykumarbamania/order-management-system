package com.project.oms.notification.service;

import com.project.oms.notification.events.NotificationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {
   @EventListener
   public void handle(NotificationEvent event) {
       // log / simulate send
   }
}
