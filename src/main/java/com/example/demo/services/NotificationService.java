package com.example.demo.services;
import com.example.demo.services.EmailSenderService;
import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    public String generateNotificationId() {
        return UUID.randomUUID().toString();
    }

    public Notification sendNotification(String userId, String role, String message, String userEmail, boolean sendEmail) {
        String id = generateNotificationId();

        Notification notification = new Notification();
        notification.setId(id);
        notification.setUserId(userId);
        notification.setRole(role);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);

        if (sendEmail && userEmail != null) {
            String subject = "Notification: " + role + " Update";
            emailSenderService.sendEmail(userEmail, subject, message);
        }
        return notification;
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
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }
    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false);
    }
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
