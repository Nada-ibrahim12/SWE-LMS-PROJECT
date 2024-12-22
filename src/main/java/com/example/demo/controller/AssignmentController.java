package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Assignment;
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

    @PostMapping("/submit")
    public ResponseEntity<String> submitAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
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
                                                         , email, false);
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

    @PutMapping("/grade")
    public ResponseEntity<Assignment> gradeAssignment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("quiz-id") Long id,
            @RequestBody String feedback) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                assignmentService.gradeAssignment(id, feedback);
                Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
                String studentId = assignment.get().getStudentId();

                String email = userService.getUserById(studentId).getEmail();
                String message = "Assignment with Title: " + assignment.get().getTitle() + " graded successfully\n Grade: " + feedback;
                notificationService.sendNotification(studentId, "Instructor", message, email, false);

                return ResponseEntity.ok(assignment.get());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(403).build();
    }
//     @GetMapping("/instructor/track/{courseId}/{studentId}")
// public ResponseEntity<List<Assignment>> trackStudentProgress(
//         @RequestHeader("Authorization") String authorizationHeader,
//         @PathVariable Long courseId,
//         @PathVariable String studentId,
//         @RequestParam(required = false) String status) {
//     try {
//         // Extract token and validate instructor role
//         String token = extractToken(authorizationHeader);
//         if (userService.hasRole(token, "Instructor")) {
//             // Fetch assignments for the student in the specific course
//             List<Assignment> assignments = (status == null) ?
//                     assignmentService.getAssignmentsByCourseAndStudent(courseId, studentId) :
//                     assignmentService.getAssignmentsByCourseAndStudentAndStatus(courseId, studentId, status);

//             return ResponseEntity.ok(assignments);
//         }
//         return ResponseEntity.status(403).body(null); // Forbidden
//     } catch (IllegalArgumentException e) {
//         return ResponseEntity.status(400).body(null); // Bad request
//     }
// }


}

