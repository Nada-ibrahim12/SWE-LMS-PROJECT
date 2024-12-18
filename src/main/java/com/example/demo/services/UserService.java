package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.user;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private JwtUtil jwtUtil = new JwtUtil();

    // ================================
    // AUTHENTICATION
    // ================================
    @PostConstruct // Initialize Admin account when application starts
    public void initializeAdmin() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            user adminUser = new user("1", "admin", jwtUtil.hashPassword("admin123"), "Admin");
            userRepository.save(adminUser);
            System.out.println("Admin user created with username: admin and password: admin123");
        }
    }
    public user getUserFromToken(String token) {
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    public String loginUser(String username, String password) {
        user user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        // Check if the user is already logged in
        if (user.isLoggedIn()) {
            throw new IllegalArgumentException("User already logged in.");
        }

        // Verify password
        if (!jwtUtil.checkPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Mark the user as logged in
        user.setLoggedIn(true);

        // Generate a JWT token for the user
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }

    public boolean logoutUser(String token) {
        String username = jwtUtil.extractUsername(token);
        Optional<user> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            user user = userOptional.get();
            user.setLoggedIn(false); // Mark the user as logged out
            return true;
        }
        return false;
    }

    public boolean hasRole(String token, String role) {
        try {
            String tokenRole = jwtUtil.extractRole(token);
            System.out.println("Extracted Role from Token: " + tokenRole); // Debug
            return role.equals(tokenRole);
        } catch (Exception e) {
            System.out.println("Error validating role: " + e.getMessage());
            return false;
        }
    }


    // ================================
    // ADMIN OPERATIONS
    // ================================

    public void registerUser(user newUser) {
        Optional<user> existingUser = userRepository.findByUsername(newUser.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        newUser.setPassword(jwtUtil.hashPassword(newUser.getPassword()));
        userRepository.save(newUser);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public java.util.List<user> getAllUsers() {
        return userRepository.findAll();
    }

    // ================================
    // INSTRUCTOR OPERATIONS
    // ================================

    public java.util.List<user> getEnrolledStudents() {
        return userRepository.findByRole("Student");
    }

    public void createCourse(String courseName) {
        // Dummy implementation
    }

    // ================================
    // STUDENT OPERATIONS
    // ================================

    // public void enrollInCourse(String token, String courseId) {
    //     String username = jwtUtil.extractUsername(token);
    //     // Dummy implementation: log the enrollment
    //     System.out.println("User " + username + " enrolled in course: " + courseId);
    // }

    // public java.util.List<String> getEnrolledCourses(String token) {
    //     // Dummy implementation
    //     return java.util.List.of("Course 1", "Course 2");
    // }

    public user getUserProfile(String token) {
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void updateUserProfile(String token, user updatedProfile) {
        String username = jwtUtil.extractUsername(token);
        user existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        existingUser.setUsername(updatedProfile.getUsername());
        existingUser.setPassword(jwtUtil.hashPassword(updatedProfile.getPassword())); // Hash the password
        userRepository.save(existingUser);
    }
}