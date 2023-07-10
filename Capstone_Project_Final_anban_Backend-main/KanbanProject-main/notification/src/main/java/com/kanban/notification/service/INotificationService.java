package com.kanban.notification.service;

import com.kanban.notification.config.NotificationDTO;
import com.kanban.notification.domain.Notification;

public interface INotificationService {
    Notification getNotification(String username);

    void saveNotification(NotificationDTO notificationDTO);

    boolean markAllRead(String username);

    boolean markRead(String username, String message);
}
