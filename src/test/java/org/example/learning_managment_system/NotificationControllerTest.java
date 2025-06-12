package org.example.learning_managment_system;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.demo.controller.NotificationController;
import com.example.demo.model.Notification;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;

class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNotifications_AdminAccess() {
        String adminToken = "Bearer admin-token";
        List<Notification> mockNotifications = new ArrayList<>();
        mockNotifications.add(new Notification());

        when(userService.hasRole("admin-token", "Admin")).thenReturn(true);
        when(notificationService.getAllNotifications()).thenReturn(mockNotifications);

        ResponseEntity<List<Notification>> response = notificationController.getAllNotifications(adminToken);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(notificationService, times(1)).getAllNotifications();
    }

    @Test
    void testGetAllNotifications_UnauthorizedAccess() {
        String userToken = "Bearer user-token";

        when(userService.hasRole("user-token", "Admin")).thenReturn(false);

        ResponseEntity<List<Notification>> response = notificationController.getAllNotifications(userToken);

        assertEquals(403, response.getStatusCodeValue());
        verify(notificationService, never()).getAllNotifications();
    }

    @Test
    void testGetNotificationById() {
        Long notificationId = 1L;
        Notification mockNotification = new Notification();

        when(notificationService.getNotificationById(notificationId)).thenReturn(Optional.of(mockNotification));

        Optional<Notification> result = notificationController.getNotificationById(notificationId);

        assertTrue(result.isPresent());
        assertEquals(mockNotification, result.get());
        verify(notificationService, times(1)).getNotificationById(notificationId);
    }

    @Test
    void testSendNotification_AdminAccess() {
        String adminToken = "Bearer admin-token";
        String userId = "user1";
        String role = "Student";
        String message = "Test Notification";
        Notification mockNotification = new Notification();

        when(userService.hasRole("admin-token", "Admin")).thenReturn(true);
        when(notificationService.sendNotification(userId, role, message, null, false)).thenReturn(mockNotification);

        ResponseEntity<Object> response = notificationController.sendNotification(adminToken, userId, role, message, null, false);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockNotification, response.getBody());
        verify(notificationService, times(1)).sendNotification(userId, role, message, null, false);
    }

    @Test
    void testDeleteNotification_UnauthorizedAccess() {
        String userToken = "Bearer user-token";
        Long notificationId = 1L;

        when(userService.hasRole("user-token", "Admin")).thenReturn(false);
        when(userService.hasRole("user-token", "Student")).thenReturn(false);
        when(userService.hasRole("user-token", "Instructor")).thenReturn(false);

        notificationController.deleteNotification(userToken, notificationId);

        verify(notificationService, never()).deleteNotification(notificationId);
    }

    @Test
    void testGetUserNotifications() {
        String userId = "user1";
        List<Notification> mockNotifications = new ArrayList<>();
        mockNotifications.add(new Notification());

        when(notificationService.getUserNotifications(userId)).thenReturn(mockNotifications);

        ResponseEntity<List<Notification>> response = notificationController.getUserNotifications(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(notificationService, times(1)).getUserNotifications(userId);
    }

    @Test
    void testMarkAsRead() {
        String notificationId = "notification1";

        doNothing().when(notificationService).markAsRead(notificationId);

        ResponseEntity<String> response = notificationController.markAsRead(notificationId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Notification marked as read", response.getBody());
        verify(notificationService, times(1)).markAsRead(notificationId);
    }
}
