package com.example.demo.repository;


import com.example.demo.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepository {
    private List<Notification> notifications = new ArrayList<>();

    public List<Notification> findAll() {
        return notifications;
    }
    public Optional<Notification> findById(Long id) {
        return notifications.stream().filter(notification -> notification.getId().equals(id)).findFirst();
    }
    public Notification save(Notification notification) {
        notifications.add(notification);
        return notification;
    }
    public void delete(Long id) {
        notifications.removeIf(notification -> notification.getId().equals(id));

    }
    public List<Notification> findByUserIdAndIsRead(String userId, boolean isRead) {
        List<Notification> notifications = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getUserId() == userId && notification.isRead() == isRead) {
                notifications.add(notification);
            }
        }
        return notifications;
    }
    public List<Notification> findByUserId(String userId) {
        List<Notification> notifications = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getUserId() == userId) {
                notifications.add(notification);
            }
        }
        return notifications;
    }
}
