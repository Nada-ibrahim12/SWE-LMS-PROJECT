package com.example.demo.controller;

import com.example.demo.model.Assignment;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.AssignmentService;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    private String extractToken(String authorizationHeader) {
        if (isValidAuthorizationHeader(authorizationHeader)) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    @PostMapping("/{course-id}/{assignemt-id}/submit")
    public ResponseEntity<String> submitAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable long assignmentId,
            @PathVariable long courseId,
            @RequestParam("assignment") String assignmentData,
            @RequestParam("file") MultipartFile file) {

        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Student")) {
                try {
                    Assignment assignment = objectMapper.readValue(assignmentData, Assignment.class);
                    assignment.setStatus("Submitted");
                    String id = userService.getUserFromToken(token).getUserId();
                    String email = userService.getUserFromToken(token).getEmail();
                    assignment.setStudentId(id);
                    assignmentService.submitAssignmentWithFile(assignment, file);
                    String message = "Assignment with Title: " + assignment.getTitle() + "submitted successfully";
                    notificationService.sendNotification(id, "Student", message
                                                         , email, true);
                    return ResponseEntity.ok("Assignment submitted successfully");
                } catch (Exception e) {
                    return ResponseEntity.status(400).body("Failed to submit assignment: " + e.getMessage());
                }
            } else {
                return ResponseEntity.status(403).body("Forbidden: Only students can submit assignments.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token or missing authorization.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Assignment>> getAssignmentById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                return ResponseEntity.ok(assignmentService.getAssignmentById(id));
            }
            return ResponseEntity.status(403).body(Optional.empty());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Optional.empty());
        }
    }

    @PutMapping("/{assignment-id}/grade")
    public ResponseEntity<Assignment> gradeAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @RequestBody String feedback) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                assignmentService.gradeAssignment(id, feedback);
                Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
                String studentId = assignment.get().getStudentId();

                String email = userService.getUserById(studentId).getEmail();
                String message = "Assignment with Title: " + assignment.get().getTitle() + " graded successfully\n Grade: " + feedback;
                notificationService.sendNotification(studentId, "Student", message, email, true);

                return ResponseEntity.ok(assignment.get());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/{course-id}/create-assignment")
    public void createAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long courseId,
            @RequestParam("assignment") Assignment assignment
            ) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                    String id = userService.getUserFromToken(token).getUserId();
                    String email = userService.getUserFromToken(token).getEmail();
                    assignmentService.createAssignment(courseId, assignment);
                    String message = "Assignment with Title: " + assignment.getTitle() + "assigned successfully";
                    notificationService.sendNotification(id, "Instructor", message
                            , email, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

