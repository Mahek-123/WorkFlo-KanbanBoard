package com.kanban.notification.repository;

import com.kanban.notification.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificationRepository extends MongoRepository<Notification, String> {
}
