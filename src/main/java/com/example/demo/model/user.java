// File: com/example/demo/model/User.java
package com.example.demo.model;

import java.util.HashMap;
import java.util.Map;

public class user {
    private String userId;
    private String username;
    private String password;
    private String role; // Admin, Instructor, or Student
    private boolean isLoggedIn;
    private String email;

    // Additional fields for progress tracking
    private Map<String, Integer> quizScores; // Quiz ID -> Score
    private Map<String, Boolean> assignmentSubmissions; // Assignment ID -> Submitted
    private int attendanceCount; // Number of classes attended

    public user(String userId, String username, String password, String role, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isLoggedIn = false; // Default to logged out
        this.email = email;

        // Initialize additional fields
        this.quizScores = new HashMap<>();
        this.assignmentSubmissions = new HashMap<>();
        this.attendanceCount = 0;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // New getters and setters for progress tracking
    public Map<String, Integer> getQuizScores() {
        return quizScores;
    }

    public void setQuizScores(Map<String, Integer> quizScores) {
        this.quizScores = quizScores;
    }

    public Map<String, Boolean> getAssignmentSubmissions() {
        return assignmentSubmissions;
    }

    public void setAssignmentSubmissions(Map<String, Boolean> assignmentSubmissions) {
        this.assignmentSubmissions = assignmentSubmissions;
    }

    public int getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(int attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public void incrementAttendance() {
        this.attendanceCount++;
    }

    public void addQuizScore(String quizId, int score) {
        this.quizScores.put(quizId, score);
    }

    public void addAssignmentSubmission(String assignmentId) {
        this.assignmentSubmissions.put(assignmentId, true);
    }
}