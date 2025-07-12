// File: com/example/demo/repository/UserRepository.java
package com.example.demo.repository;

import com.example.demo.model.user;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepository {

    private final List<user> userList = new ArrayList<>();
    private long userIdCounter = 2;


    public user save(user user) {
        Optional<user> existingUser = userList.stream()
                .filter(u -> u.getUserId().equals(user.getUserId()))
                .findFirst();

        if (existingUser.isPresent()) {
            // Update existing user
            user existing = existingUser.get();
            existing.setUsername(user.getUsername());
            existing.setPassword(user.getPassword());
            existing.setRole(user.getRole());
            existing.setLoggedIn(user.isLoggedIn());
            return existing;
        } else {
            if (user.getUserId() == null) {
                user.setUserId(String.valueOf(userIdCounter++));
            }
            userList.add(user);
            return user;
        }
    }
    public Optional<user> findByUsername(String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<user> findById(String id) {
        return userList.stream()
                .filter(user -> user.getUserId().equals(id))
                .findFirst();
    }

    public List<user> findAll() {
        return userList;
    }

    public List<user> findByRole(String role) {
        return userList.stream()
                .filter(user -> user.getRole().equals(role))
                .collect(Collectors.toList());
    }

    public void deleteByUsername(String username) {
        userList.removeIf(user -> user.getUsername().equals(username));
    }

    public boolean logoutUser(String username) {
        Optional<user> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            user user = userOptional.get();
            user.setLoggedIn(false);
            return true;
        }
        return false;
    }
    public void addQuizScore(String username, String quizId, int score) {
        findByUsername(username).ifPresent(user -> user.getQuizScores().put(quizId, score));
    }

    public void addAssignmentSubmission(String username, String assignmentId) {
        findByUsername(username).ifPresent(user -> user.getAssignmentSubmissions().put(assignmentId, true));
    }

    public void incrementAttendance(String username) {
        findByUsername(username).ifPresent(user::incrementAttendance);
    }
}