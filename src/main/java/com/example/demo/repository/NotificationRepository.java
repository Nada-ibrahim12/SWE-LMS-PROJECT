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



    public Notification save(Notification notification) {
        notifications.add(notification);
        return notification;
    }

    public Notification findById(String id) {
        System.out.println("Finding Notification with ID: " + id); // Debug log

        // Find the notification
        return notifications.stream()
                .filter(notification -> notification.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Notification with ID " + id + " not found"));
    }

    public void delete(String id) { // Use String for id
        notifications.removeIf(notification -> notification.getId().equals(id));
    }

    public List<Notification> findByUserIdAndIsRead(String userId, boolean isRead) {
        List<Notification> filteredNotifications = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getUserId().equals(userId) && notification.isRead() == isRead) {
                filteredNotifications.add(notification);
            }
        }
        return filteredNotifications;
    }

    public List<Notification> findByUserId(String userId) {
        List<Notification> filteredNotifications = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getUserId().equals(userId)) {
                filteredNotifications.add(notification);
            }
        }
        return filteredNotifications;
    }
}