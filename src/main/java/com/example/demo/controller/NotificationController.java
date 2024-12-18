package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    private String extractToken(String authorizationHeader) {
        if (isValidAuthorizationHeader(authorizationHeader)) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    @GetMapping("/admin/all")
    public List<Notification> getAllNotifications(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Admin")) {
                return notificationService.getAllNotifications();
            }
            return (List<Notification>) ResponseEntity.status(403).body("Unauthorized");
        } catch (IllegalArgumentException e) {
            return (List<Notification>) ResponseEntity.status(400).body(e.getMessage());
        } catch (RuntimeException e) {
            return (List<Notification>) ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Optional<Notification> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    @PostMapping("/admin/send")
    public ResponseEntity<Object> sendNotification(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String userId,
            @RequestParam String role,
            @RequestParam String message,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "false") boolean sendEmail) {

        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Admin")) {
                if ( userId == null || role == null || message == null) {
                    return ResponseEntity.badRequest().body("Missing required parameters.");
                }
                try {
                    Notification notification = notificationService.sendNotification(userId, role, message, email, sendEmail);
                    return ResponseEntity.ok(notification);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending the notification.");
                }
            }
            return ResponseEntity.status(403).body("Unauthorized");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @DeleteMapping("/delete/{id}")
    public void deleteNotification(@RequestHeader("Authorization") String authorizationHeader,
                                   @PathVariable Long id) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Student") ||
                userService.hasRole(token, "Admin") ||
                userService.hasRole(token, "Instructor")) {
                notificationService.deleteNotification(id);
            }
             ResponseEntity.status(403).body("Unauthorized");
        } catch (IllegalArgumentException e) {
             ResponseEntity.status(400).body(e.getMessage());
        } catch (RuntimeException e) {
             ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
    @PutMapping("/mark-read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }
}
