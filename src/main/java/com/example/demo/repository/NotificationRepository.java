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
}
