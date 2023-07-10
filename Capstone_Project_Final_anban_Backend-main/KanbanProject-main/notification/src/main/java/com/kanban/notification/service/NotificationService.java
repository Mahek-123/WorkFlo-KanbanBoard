package com.kanban.notification.service;

import com.kanban.notification.config.NotificationDTO;
import com.kanban.notification.domain.Notification;
import com.kanban.notification.repository.INotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    INotificationRepository notificationRepository;

    @Override
    public Notification getNotification(String username) {
        return notificationRepository.findById(username).get();
    }

    @RabbitListener(queues = "user-notification-queue")
    @Override
    public void saveNotification(NotificationDTO notificationDTO) {
        String name = notificationDTO.getJsonObject().get("username").toString();
        if (notificationRepository.findById(name).isEmpty()) {
            Notification notification = new Notification();
            Map<String, Boolean> messages = new HashMap();
            messages.put(notificationDTO.getJsonObject().get("Notification").toString(), false);
            notification.setNotificationMessage(messages);
            notification.setJsonObject(notificationDTO.getJsonObject());
            notification.setUsername(name);
            notificationRepository.save(notification);
        }
        Notification notification = notificationRepository.findById(name).get();
        Map<String, Boolean> messages = notification.getNotificationMessage();
        messages.put(notificationDTO.getJsonObject().get("Notification").toString(), false);
        notification.setNotificationMessage(messages);
        notificationRepository.save(notification);

    }

    @Override
    public boolean markAllRead(String username) {
        Notification notification = notificationRepository.findById(username).get();
        Map<String, Boolean> messages = notification.getNotificationMessage();
        messages.replaceAll((s, v) -> true);
        notification.setNotificationMessage(messages);
        notificationRepository.save(notification);
        return true;
    }

    @Override
    public boolean markRead(String username, String message) {
        Notification notification = notificationRepository.findById(username).get();
        Map<String, Boolean> messages = notification.getNotificationMessage();
        if (messages.containsKey(message)) {
            messages.put(message, true);
        }
        notification.setNotificationMessage(messages);
        notificationRepository.save(notification);
        return true;
    }
}
