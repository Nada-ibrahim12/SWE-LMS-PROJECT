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
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Admin")) {
                List<Notification> notifications = notificationService.getAllNotifications();
                return ResponseEntity.ok(notifications); // Return the list of notifications
            }
            return ResponseEntity.status(403).build(); // Return 403 Forbidden if not authorized
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build(); // Return 400 Bad Request for invalid authorization header
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null); // Return 500 Internal Server Error for unexpected issues
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
            Notification notification = notificationService.sendNotification(userId, role, message, email, sendEmail);
            return ResponseEntity.ok(notification);
        //    return ResponseEntity.status(403).body("Unauthorized");
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
        if (notifications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(notifications); // Return 204 if no notifications are found
        }
        return ResponseEntity.ok(notifications);
    }
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
    @PutMapping("/mark-read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable String notificationId) { // Use String for id
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok("Notification marked as read");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found");
        }
    }
}
