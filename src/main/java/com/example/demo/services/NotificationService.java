package com.example.demo.services;
import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    public Notification sendNotification(Long id, Long userId, String role, String message) {
        Notification notification = new Notification();
        notification.setId(id);
        notification.setUserId(userId);
        notification.setRole(role);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }
//    public Notification saveNotification(Notification notification) {
//        return notificationRepository.save(notification);
//    }
    public void deleteNotification(Long id) {
        notificationRepository.delete(id);
    }
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false);
    }
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
