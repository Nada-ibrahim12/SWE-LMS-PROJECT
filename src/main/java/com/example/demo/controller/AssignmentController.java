package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.model.Assignment;
import com.example.demo.model.AssignmentSubmission;
import com.example.demo.services.AssignmentService;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
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

    // Assignment Submissions

    @PostMapping("/student/submit-assignment")
    public ResponseEntity<String> submitAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("assignment") String assignmentData,
            @RequestParam("file") MultipartFile file) {

        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Student")) {
                try {
                    AssignmentSubmission assignment = objectMapper.readValue(assignmentData, AssignmentSubmission.class);
                    assignment.setStatus("Submitted");
                    String id = userService.getUserFromToken(token).getUserId();
                    String email = userService.getUserFromToken(token).getEmail();
                    assignment.setStudentId(id);
                    assignmentService.submitAssignmentWithFile(id, assignment, file);
                    String message = "Assignment submitted successfully";
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

    @GetMapping("/instructor/submissions/{id}")
    public ResponseEntity<?> getAssignmentSubmissionById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                Optional<AssignmentSubmission> assignmentOpt = assignmentService.getAssignmentSubmissionById(id);

                if (assignmentOpt.isPresent()) {
                    return ResponseEntity.ok(assignmentOpt.get());
                } else {
                    return ResponseEntity.status(404).body("Assignment not found with ID: " + id);
                }
            } else {
                return ResponseEntity.status(403).body("Forbidden: Only instructors can access this resource.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Invalid token or missing authorization header.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/instructor/submissions/all")
    public ResponseEntity<List<AssignmentSubmission>> getAllSubmissions(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "studentId", required = false) String studentId,
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "status", required = false) String status) {
        try {
            String token = extractToken(authorizationHeader);
            if (!userService.hasRole(token, "Instructor")) {
                return ResponseEntity.status(403).body(null);
            }

            List<AssignmentSubmission> allAssignments = assignmentService.findAllSubmissions();
            List<AssignmentSubmission> filteredAssignments = allAssignments.stream()
                    .filter(assignment -> studentId == null || assignment.getStudentId().equals(studentId))
                    .filter(assignment -> status == null || assignment.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredAssignments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @PutMapping("/grade")
    public ResponseEntity<?> gradeAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("quiz-id") Long id,
            @RequestBody Long score) {
        try {
            String token = extractToken(authorizationHeader);
            if (!userService.hasRole(token, "Instructor")) {
                return ResponseEntity.status(403).body("Forbidden: Only instructors can grade assignments.");
            }

            Optional<AssignmentSubmission> assignmentOpt = assignmentService.getAssignmentSubmissionById(id);
            if (!assignmentOpt.isPresent()) {
                return ResponseEntity.status(404).body("Assignment not found with ID: " + id);
            }

            AssignmentSubmission assignment = assignmentOpt.get();
            assignmentService.gradeAssignment(id, score);

            String studentId = assignment.getStudentId();
            String email = userService.getUserById(studentId).getEmail();
            String message = String.format("Assignment graded successfully. Grade: %d", score);

            notificationService.sendNotification(studentId, "Student", message, email, true);
            return ResponseEntity.ok(assignment);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }

    // Assignment

    @PostMapping("/instructor/create-assignment")
    public ResponseEntity<String> createAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("assignment") String assignmentData,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                try {
                    Assignment assignment = objectMapper.readValue(assignmentData, Assignment.class);

                    String instructorId = userService.getUserFromToken(token).getUserId();
                    String email = userService.getUserFromToken(token).getEmail();

                    assignment.setInstructorId(instructorId);

                    assignmentService.createAssignment(assignment, file);

                    String message = "Assignment created successfully.";
                    notificationService.sendNotification(instructorId, "Instructor", message, email, true);

                    return ResponseEntity.ok("Assignment created successfully.");
                } catch (Exception e) {
                    return ResponseEntity.status(400).body("Failed to create assignment: " + e.getMessage());
                }
            } else {
                return ResponseEntity.status(403).body("Forbidden: Only instructors can create assignments.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token or missing authorization.");
        }
    }

    @GetMapping("/instructor/{id}")
    public ResponseEntity<?> getAssignmentById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                Optional<Assignment> assignmentOpt = assignmentService.getAssignment(id);

                if (assignmentOpt.isPresent()) {
                    return ResponseEntity.ok(assignmentOpt.get());
                } else {
                    return ResponseEntity.status(404).body("Assignment not found with ID: " + id);
                }
            } else {
                return ResponseEntity.status(403).body("Forbidden: Only instructors can access this resource.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Invalid token or missing authorization header.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/instructor/all")
    public ResponseEntity<List<Assignment>> getAllAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "studentId", required = false) String studentId,
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "status", required = false) String status) {
        try {
            String token = extractToken(authorizationHeader);
            if (!userService.hasRole(token, "Instructor")) {
                return ResponseEntity.status(403).body(null);
            }
            List<Assignment> allAssignments = assignmentService.findAllAssignments();
            return ResponseEntity.ok(allAssignments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Assignment>> searchAssignments(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("keyword") String keyword) {
        try {
            String token = extractToken(authorizationHeader);
            if (!userService.hasRole(token, "Instructor")) {
                return ResponseEntity.status(403).body(null);
            }

            List<Assignment> results = assignmentService.searchAssignments(keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}

