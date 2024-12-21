package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AssignmentRecord;
import com.example.demo.model.PerformanceRecord;
import com.example.demo.services.PerformanceTrackingService;

@RestController
@RequestMapping("/performance")
public class PerformanceTrackingController {

    @Autowired
    private PerformanceTrackingService performanceTrackingService;

    @GetMapping("/instructor/{instructorId}/course/{courseId}/student/{studentId}/quiz-scores")
    public ResponseEntity<List<PerformanceRecord>> getQuizScores(@PathVariable String instructorId,
            @PathVariable Long courseId,
            @PathVariable String studentId) {
        List<PerformanceRecord> performanceRecords = performanceTrackingService
                .trackQuizScoresByStudentId(instructorId, courseId, studentId);
        return ResponseEntity.status(HttpStatus.OK).body(performanceRecords);
    }

    @GetMapping("/instructor/{instructorId}/course/{courseId}/student/{studentId}/assignments")
    public ResponseEntity<List<AssignmentRecord>> getAssignments(@PathVariable String instructorId,
            @PathVariable Long courseId,
            @PathVariable String studentId) {
        List<AssignmentRecord> performanceRecords = performanceTrackingService
                .trackAssignmentsByStudentId(instructorId, courseId, studentId);
        return ResponseEntity.status(HttpStatus.OK).body(performanceRecords);

    }

    @GetMapping("/instructor/{instructorId}/course/{courseId}/getAllStudentsQuizzes")
    public ResponseEntity<List<PerformanceRecord>> getAllStudentsQuizzes(@PathVariable String instructorId, @PathVariable Long courseId) {
        List<PerformanceRecord> performanceRecords = performanceTrackingService
                .trackQuizScoresForAllStudentsWithNames(instructorId, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(performanceRecords);

    }

    @GetMapping("/instructor/{instructorId}/course/{courseId}/getAllStudentsAssignments")
    public ResponseEntity<List<AssignmentRecord>> getAllStudentsAssignments(@PathVariable String instructorId, @PathVariable Long courseId) {
        List<AssignmentRecord> performanceRecords = performanceTrackingService
                .trackAssignmentsForAllStudentsWithNames(instructorId, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(performanceRecords);

    }

}

// public class PerformanceTrackingController {
//     @Autowired
//     private PerformanceTrackingService performanceTrackingService;
//     @Autowired
//     private UserService userService;
//     /**
//      * Extracts the token from the Authorization header.
//      */
//     private String extractToken(String authorizationHeader) {
//         if (isValidAuthorizationHeader(authorizationHeader)) {
//             return authorizationHeader.replace("Bearer ", "");
//         }
//         throw new IllegalArgumentException("Invalid or missing Authorization header");
//     }
//     /**
//      * Validates the Authorization header format.
//      */
//     private boolean isValidAuthorizationHeader(String authorizationHeader) {
//         return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
//     }
//     /**
//      * Validates user role based on token and required role.
//      */
//     private void validateUserRole(String token, String requiredRole) {
//         if (!userService.hasRole(token, requiredRole)) {
//             throw new SecurityException("User does not have the required role: " + requiredRole);
//         }
//     }
//     /**
//      * API to track quiz scores for a specific student.
//      */
//     @GetMapping("/getQuizScores/{studentId}")
//     public ResponseEntity<?> trackQuizScores(
//             @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
//             @PathVariable String studentId) {
//         try {
//             // Check if Authorization header is present
//             if (authorizationHeader == null || authorizationHeader.isEmpty()) {
//                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header is missing");
//             }
//             // Extract and validate token
//             String token = extractToken(authorizationHeader);
//             validateUserRole(token, "Admin"); // Ensure Admin or Instructor role
//             // Fetch quiz scores
//             List<PerformanceRecord> quizScores = performanceTrackingService.trackQuizScoresByStudentId(studentId);
//             return ResponseEntity.ok(quizScores);
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//         } catch (SecurityException e) {
//             return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
//         }
//     }
//     /**
//      * API to track attendance for a specific student.
//      */
//     // @GetMapping("/getAttendance/{studentId}")
//     // public ResponseEntity<?> trackAttendance(
//     //         @RequestHeader("Authorization") String authorizationHeader,
//     //         @PathVariable String studentId) {
//     //     try {
//     //         String token = extractToken(authorizationHeader);
//     //         validateUserRole(token, "Instructor"); // Instructor role required
//     //         List<PerformanceRecord> attendanceRecords = performanceTrackingService.trackAttendanceByStudentId(studentId);
//     //         return ResponseEntity.ok(attendanceRecords);
//     //     } catch (IllegalArgumentException e) {
//     //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//     //     } catch (SecurityException e) {
//     //         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//     //     } catch (Exception e) {
//     //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
//     //     }
//     // }
//     // /**
//     //  * API to track assignment submissions for a specific student.
//     //  */
//     // @GetMapping("/getAssignmentSubmissions/{studentId}")
//     // public ResponseEntity<?> trackAssignmentSubmissions(
//     //         @RequestHeader("Authorization") String authorizationHeader,
//     //         @PathVariable String studentId) {
//     //     try {
//     //         String token = extractToken(authorizationHeader);
//     //         validateUserRole(token, "Admin"); // Admin role required
//     //         List<PerformanceRecord> submissions = performanceTrackingService.trackAssignmentSubmissionsByStudentId(studentId);
//     //         return ResponseEntity.ok(submissions);
//     //     } catch (IllegalArgumentException e) {
//     //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//     //     } catch (SecurityException e) {
//     //         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//     //     } catch (Exception e) {
//     //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
//     //     }
//     // }
// }
// /*Performance Tracking & Bonus Features
// Tasks:
// Implement PerformanceTracking module:
// Develop methods to track quiz scores, assignment submissions, and attendance.
// Create APIs for instructors and admins to fetch progress reports.
// Implement bonus features:
// Generate Excel reports for performance data using a library like Apache POI.
// Create visual progress reports (charts) using a library like Chart.js (for frontend or integration).
// Handle email notifications for important events (graded assignments, enrollments).
// Write unit tests for performance tracking logic. */
// package com.example.demo.controller;
// import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import com.example.demo.model.PerformanceRecord;
// import com.example.demo.services.PerformanceTrackingService;
// import com.example.demo.services.UserService;
// @RestController
// @RequestMapping("/performance")
// public class PerformanceTrackingController {
//     @Autowired
//     private PerformanceTrackingService performanceTrackingService;
//     @Autowired
//     private UserService userService;
//     private String extractToken(String authorizationHeader) {
//         if (isValidAuthorizationHeader(authorizationHeader)) {
//             return authorizationHeader.replace("Bearer ", "");
//         }
//         throw new IllegalArgumentException("Invalid Authorization header");
//     }
//     private boolean isValidAuthorizationHeader(String authorizationHeader) {
//         return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
//     }
//     @GetMapping("/getQuizScores/{studentId}/")
//     public ResponseEntity<List<PerformanceRecord>> trackQuizScores(
//             @RequestHeader("Authorization") String authorizationHeader,
//             @PathVariable String studentId) {
//         try {
//             String token = extractToken(authorizationHeader);
//             if (userService.hasRole(token, "Admin") || userService.hasRole(token, "Instructor")) {
//                 List<PerformanceRecord> quizScores = performanceTrackingService.trackQuizScoresByStudentId(studentId);
//                 return ResponseEntity.ok(quizScores);
//             }
//             return ResponseEntity.status(403).body(null);
//         } catch (RuntimeException e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//         }
//     }
//     // @GetMapping("/student/{studentId}/assignments")
//     // public ResponseEntity<List<PerformanceRecord>> trackAssignments(
//     //         @RequestHeader("Authorization") String authorizationHeader, 
//     //         @PathVariable String studentId) {
//     //     try {
//     //         String token = extractToken(authorizationHeader);
//     //         if (userService.hasRole(token, "Admin") || userService.hasRole(token, "Instructor")) {
//     //             List<PerformanceRecord> assignments = performanceTrackingService.trackAssignmentSubmissions(studentId);
//     //             return ResponseEntity.ok(assignments);
//     //         }
//     //         return ResponseEntity.status(403).body(null);
//     //     } catch (IllegalArgumentException | RuntimeException e) {
//     //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//     //     }
//     // }
//     // @GetMapping("/student/{studentId}/attendance")
//     // public ResponseEntity<List<PerformanceRecord>> trackAttendance(
//     //         @RequestHeader("Authorization") String authorizationHeader, 
//     //         @PathVariable String studentId) {
//     //     try {
//     //         String token = extractToken(authorizationHeader);
//     //         if (userService.hasRole(token, "Admin") || userService.hasRole(token, "Instructor")) {
//     //             List<PerformanceRecord> attendance = performanceTrackingService.trackAttendance(studentId);
//     //             return ResponseEntity.ok(attendance);
//     //         }
//     //         return ResponseEntity.status(403).body(null);
//     //     } catch (IllegalArgumentException | RuntimeException e) {
//     //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//     //     }
//     // }
//     // @GetMapping("/student/{studentId}/progress")
//     // public ResponseEntity<List<PerformanceRecord>> getStudentProgressReport(
//     //         @RequestHeader("Authorization") String authorizationHeader, 
//     //         @PathVariable String studentId) {
//     //     try {
//     //         String token = extractToken(authorizationHeader);
//     //         if (userService.hasRole(token, "Admin") || userService.hasRole(token, "Instructor")) {
//     //             List<PerformanceRecord> progressReport = performanceTrackingService.getProgressReport(studentId);
//     //             return ResponseEntity.ok(progressReport);
//     //         }
//     //         return ResponseEntity.status(403).body(null);
//     //     } catch (IllegalArgumentException | RuntimeException e) {
//     //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//     //     }
//     // }
// }
// /*
//     public List<PerformanceRecord> trackQuizScoresByStudentId(String studentId) {
//         List<PerformanceRecord> records = new ArrayList<>();
//         List<QuizSubmission> submissions = quizRepository.findAllSubmissions()
//                 .stream().filter(Q -> Q.getStudentId().equals(studentId)).toList();
//         for (QuizSubmission quizSubmission : submissions) {
//             PerformanceRecord record = new PerformanceRecord();
//             String description = quizRepository.findById(quizSubmission.getQuizId()).getDescription();
//             double score = (double) quizSubmission.getScore();
//             record.setType("Quiz");
//             record.setDescription(description);
//             record.setScoreOrStatus(score);
//             records.add(record);
//         }
//         return records;
//     }
//  */
// // @GetMapping("/{instructorId}")
// // public ResponseEntity<List<PerformanceTracking>> getPerformanceByInstructorId(@PathVariable Long instructorId) {
// //     List<PerformanceTracking> performance = performanceTrackingService.getPerformanceByInstructorId(instructorId);
// //     return ResponseEntity.ok(performance);
// // }
// // @PostMapping("/add")
// // public ResponseEntity<PerformanceTracking> createPerformanceTracking(@RequestBody PerformanceTracking performanceTracking) {
// //     PerformanceTracking createdPerformance = performanceTrackingService.createPerformanceTracking(performanceTracking);
// //     return ResponseEntity.status(201).body(createdPerformance);
// // }

