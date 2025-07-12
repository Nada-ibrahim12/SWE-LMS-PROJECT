package com.example.demo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.QuestionBank;
import com.example.demo.model.user;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

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


    @PostMapping("/instructor/create-course")
    public ResponseEntity<String> createCourse(@RequestBody Course course,@RequestHeader("Authorization") String authorizationHeader) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Instructor")) {
            courseService.createCourse(course, token);
            return ResponseEntity.ok("Course created successfully");
        }
        return ResponseEntity.status(403).body("Access denied. Instructor role required.");
    }

    @PostMapping("/instructor/{courseId}/create-QB")
    public ResponseEntity<String> createQuestionBank(@RequestHeader("Authorization") String authorizationHeader,
                                                     @RequestBody QuestionBank questionBank,
                                                     @PathVariable Long courseId) {
       
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body("Missing or invalid Authorization header");
        }
        String token = authorizationHeader.replace("Bearer ", "");

        if (userService.hasRole(token, "Instructor")) {
            
            courseService.createQuestionBank(courseId, questionBank, token);
            return ResponseEntity.ok("QuestionBank created successfully");
        }

        return ResponseEntity.status(403).body("Access denied. Instructor role required.");
    }

    @PostMapping("/{courseId}/lessons")
    public ResponseEntity<String> addLessonToCourse(@PathVariable Long courseId, @RequestBody Lesson lesson,
                                                    @RequestHeader("Authorization") String authorizationHeader) {
        // Check if the Authorization header is valid
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body("Missing or invalid Authorization header");
        }
        String token = authorizationHeader.replace("Bearer ", "");

        if (userService.hasRole(token, "Instructor")) {

            try {

                Lesson savedLesson = lessonService.addLessonToCourse(courseId, lesson);
                courseService.addLessonToCourse(courseId, lesson, token);
                return ResponseEntity.ok("Lesson added successfully with ID: " + savedLesson.getId());
            } catch (RuntimeException e) {
                return ResponseEntity.status(404).body("Course not found: " + e.getMessage());
            }
        }

        return ResponseEntity.status(403).body("Access denied. Instructor role required.");
    }


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

    // ================================
    // STUDENT ENDPOINTS
    // ================================

    @PostMapping("/student/{courseId}/enroll")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId,@RequestHeader("Authorization") String authorizationHeader) {
    if (!isValidAuthorizationHeader(authorizationHeader)) {
        return ResponseEntity.status(400).body("Missing or invalid Authorization header");
    }

    String token = authorizationHeader.replace("Bearer ", "");
    if (userService.hasRole(token, "Student")) {
        enrollmentService.enrollInCourse(token, courseId);
        return ResponseEntity.ok("Enrolled successfully");
    }
    return ResponseEntity.status(403).body("Access denied. Student role required.");
}

    // ================================
    // COURSE ENDPOINTS
    // ================================

    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/courses/{courseId}/enrolled")
    public ResponseEntity<?> viewEnrolled(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long courseId) {
        try {
            // Validate Authorization header
            String token = extractToken(authorizationHeader);

            // Verify role (Instructor or Admin)
            if (userService.hasRole(token, "Instructor") || userService.hasRole(token, "Admin")) {
                List<user> enrolledStudents = courseService.viewEnrolledStudents(courseId, token);

                if (enrolledStudents.isEmpty()) {
                    return ResponseEntity.noContent().build(); // 204 No Content
                }
                return ResponseEntity.ok(enrolledStudents); // 200 OK with enrolled students
            }

            return ResponseEntity.status(403).body("Access denied: Only Instructors or Admins can view enrolled students.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid authorization header.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }
@GetMapping("/courses/available")
public ResponseEntity<List<Course>> viewAvailableCourses(@RequestHeader("Authorization") String authorizationHeader) {
    if (!isValidAuthorizationHeader(authorizationHeader)) {
        return ResponseEntity.status(400).body(null);
    }

    String token = authorizationHeader.replace("Bearer ", "");
    if (userService.hasRole(token, "Student")) {
        try {
            List<Course> availableCourses = courseService.viewAvailableCourses(token);
            return ResponseEntity.ok(availableCourses);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(null);
        }
    }
    return ResponseEntity.status(403).body(null);
}

@PostMapping("/instructor/{courseId}/upload-media")
public ResponseEntity<String> uploadMediaFile(@RequestHeader("Authorization") String authorizationHeader,
                                              @PathVariable Long courseId,
                                              @RequestParam("file") MultipartFile file) throws IOException, java.io.IOException {
    String token = extractToken(authorizationHeader);

    try {
        if (!userService.hasRole(token, "Instructor")) {
            return ResponseEntity.status(403).body("Access Denied: Only instructors can upload media files.");
        }
        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        if (file.isEmpty()) {
            return ResponseEntity.status(400).body("Please select a file to upload.");
        }
        String baseDirectory = System.getProperty("user.dir"); 
        String uploadDirectory = baseDirectory + "/uploads/media/" + courseId + "/"; 
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Path path = Paths.get(uploadDirectory + file.getOriginalFilename());

        try {
            Files.write(path, file.getBytes());
            if (course.getMediaFiles() == null) {
                course.setMediaFiles(new ArrayList<>());
            }
            course.getMediaFiles().add(file.getOriginalFilename()); 
            courseService.saveCourse(course);

        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body("Failed to save file: " + e.getMessage());
        }
        return ResponseEntity.status(200).body("File successfully uploaded for Course ID " 
                + courseId + ": " + file.getOriginalFilename());

    } catch (RuntimeException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
private String extractToken(String authorizationHeader) {
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        return authorizationHeader.replace("Bearer ", "");
    } else {
        throw new IllegalArgumentException("Invalid Authorization header");
    }
}


    // ================================
    // Helper Methods
    // ================================

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }
    @GetMapping("/profile")
    public ResponseEntity<user> viewProfile(@RequestHeader("Authorization") String authorizationHeader) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body(null);
        }
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(userService.getUserProfile(token));
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestHeader("Authorization") String authorizationHeader,
                                                @RequestBody user updatedProfile) {
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return ResponseEntity.status(400).body("Missing or invalid Authorization header");
        }

        try {
            String token = authorizationHeader.replace("Bearer ", "");
            userService.updateUserProfile(token, updatedProfile);
            return ResponseEntity.ok("Profile updated successfully. Please log in again.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }
    @PostMapping("/instructor/add-quiz-score")
    public ResponseEntity<String> addQuizScore(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestParam String studentUsername,
                                               @RequestParam String quizId,
                                               @RequestParam int score) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Instructor")) {
            userService.addQuizScore(studentUsername, quizId, score);
            return ResponseEntity.ok("Quiz score added successfully.");
        }
        return ResponseEntity.status(403).body("Access denied.");
    }

    @PostMapping("/instructor/submit-assignment")
    public ResponseEntity<String> submitAssignment(@RequestHeader("Authorization") String authorizationHeader,
                                                   @RequestParam String studentUsername,
                                                   @RequestParam String assignmentId) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Instructor")) {
            userService.addAssignmentSubmission(studentUsername, assignmentId);
            return ResponseEntity.ok("Assignment submission recorded.");
        }
        return ResponseEntity.status(403).body("Access denied.");
    }

    @PostMapping("/instructor/increment-attendance")
    public ResponseEntity<String> incrementAttendance(@RequestHeader("Authorization") String authorizationHeader,
                                                      @RequestParam String studentUsername) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Instructor")) {
            userService.incrementAttendance(studentUsername);
            return ResponseEntity.ok("Attendance incremented.");
        }
        return ResponseEntity.status(403).body("Access denied.");
    }

    @GetMapping("/admin/generate-report")
    public ResponseEntity<String> generateReport(@RequestHeader("Authorization") String authorizationHeader,
                                                 @RequestParam String fileName) throws IOException, java.io.IOException {
        String token = authorizationHeader.replace("Bearer ", "");
        if (userService.hasRole(token, "Admin")) {
            return ResponseEntity.ok(userService.generatePerformanceReport(fileName));
        }
        return ResponseEntity.status(403).body("Access denied.");
    }
}

