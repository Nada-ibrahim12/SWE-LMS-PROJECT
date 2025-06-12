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
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    public String generateNotificationId() {
        AtomicLong counter = new AtomicLong(1);
        long id = counter.getAndIncrement(); // Increment and get the next value
        System.out.println(id);
        return String.valueOf(id); // Return as a string
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
        return Optional.ofNullable(notificationRepository.findById(String.valueOf(id)));
    }

    public void deleteNotification(Long id) {
        notificationRepository.delete(String.valueOf(id));
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId); // This should return the notifications for a specific user
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false);
    }

    public void markAsRead(String notificationId) {
        System.out.println(notificationId);
        // Fetch the notification by ID
        Notification notification = notificationRepository.findById(notificationId)
                ;
        // Update the `isRead` attribute
        notification.setRead(true);

        // Save the updated notification back to the repository
        notificationRepository.save(notification);
    }
}