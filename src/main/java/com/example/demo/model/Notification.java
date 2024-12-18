package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String message;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String role;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    private LocalDateTime timestamp;

    public Notification() {}

    public Notification(String id,String message, String userId, String role, boolean isRead, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.userId = userId;
        this.role = role;
        this.isRead = false;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
