// File: com/example/demo/controller/UserController.java
package com.example.demo.controller;

import com.example.demo.model.user;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ================================
    // AUTHENTICATION ENDPOINTS
    // ================================

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody user loginRequest) {
        try {
            String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok("Bearer " + token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.logoutUser(token)) {
            return ResponseEntity.ok("Logout successful");
        } else {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }

    // ================================
    // ADMIN ENDPOINTS
    // ================================

    @PostMapping("/admin/register-user")
    public ResponseEntity<String> registerUser(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestBody user newUser) {

        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        System.out.println("Authorization Token: " + token); // Debugging

        if (userService.hasRole(token, "Admin")) {
            try {
                userService.registerUser(newUser);
                return ResponseEntity.status(201).body("User registered successfully");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(400).body(e.getMessage());
            }
        }
        return ResponseEntity.status(403).body("Access denied. Admin role required.");
    }


    @GetMapping("/admin/all-users")
    public ResponseEntity<List<user>> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body(null);
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Admin")) {
            return ResponseEntity.ok(userService.getAllUsers());
        }
        return ResponseEntity.status(403).body(null);
    }

    @DeleteMapping("/admin/delete-user")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestParam String username) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Admin")) {
            userService.deleteUser(username);
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.status(403).body("Access denied. Admin role required.");
    }

    // ================================
    // INSTRUCTOR ENDPOINTS
    // ================================

    @GetMapping("/instructor/students")
    public ResponseEntity<List<user>> getStudents(@RequestHeader("Authorization") String authorizationHeader) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body(null);
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Instructor")) {
            return ResponseEntity.ok(userService.getEnrolledStudents());
        }
        return ResponseEntity.status(403).body(null);
    }

    @PostMapping("/instructor/create-course")
    public ResponseEntity<String> createCourse(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestBody String courseName) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Instructor")) {
            //  userService.createCourse(courseName);
            return ResponseEntity.ok("Course created successfully");
        }
        return ResponseEntity.status(403).body("Access denied. Instructor role required.");
    }

    // ================================
    // STUDENT ENDPOINTS
    // ================================

    @PostMapping("/student/enroll")
    public ResponseEntity<String> enrollInCourse(@RequestHeader("Authorization") String authorizationHeader,
                                                 @RequestBody String courseId) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Student")) {
            // userService.enrollInCourse(token, courseId);
            return ResponseEntity.ok("Enrolled successfully");
        }
        return ResponseEntity.status(403).body("Access denied. Student role required.");
    }

    @GetMapping("/student/courses")
    public ResponseEntity<List<String>> getCourses(@RequestHeader("Authorization") String authorizationHeader) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body(null);
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Student")) {
            //return ResponseEntity.ok(userService.getEnrolledCourses(token));
        }
        return ResponseEntity.status(403).body(null);
    }

    // ================================
    // Helper Method
    // ================================

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }
}