package com.example.demo.controller;

import com.example.demo.model.Assignment;
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

    private String extractToken(String authorizationHeader) {
        if (isValidAuthorizationHeader(authorizationHeader)) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitAssignment(
            @RequestParam("assignment") String assignmentData,
            @RequestParam("file") MultipartFile file) {
        try {
            Assignment assignment = objectMapper.readValue(assignmentData, Assignment.class);
            assignment.setStatus("Submitted");
            assignmentService.submitAssignmentWithFile(assignment, file);
            return ResponseEntity.ok("Assignment submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to submit assignment: " + e.getMessage());
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

    @PutMapping("/{id}/grade")
    public ResponseEntity<Optional<Object>> gradeAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id, @RequestBody String feedback) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                assignmentService.gradeAssignment(id, feedback);
            }
            return ResponseEntity.status(403).body(Optional.empty());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Optional.empty());
        }
    }
}

