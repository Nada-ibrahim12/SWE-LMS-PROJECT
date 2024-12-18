package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.QuestionBank;
import com.example.demo.model.user;
import com.example.demo.services.CourseService;
import com.example.demo.services.EnrollmentService;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

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
    if (!isValidAuthorizationHeader(authorizationHeader)) {
        return ResponseEntity.status(400).body("Missing or invalid Authorization header");
    }

    String token = authorizationHeader.replace("Bearer ", "");
    if (userService.hasRole(token, "Instructor")) {
        courseService.addLessonToCourse(courseId, lesson, token);
        return ResponseEntity.ok("Lesson added successfully");
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

    @GetMapping("/instructor/{courseId}/enrolled")
public ResponseEntity<List<user>> viewEnrolledStudents(@RequestHeader("Authorization") String authorizationHeader, 
                                                        @PathVariable Long courseId) {
    if (!isValidAuthorizationHeader(authorizationHeader)) {
        return ResponseEntity.status(400).body(null); 
    }

    String token = authorizationHeader.replace("Bearer ", "");
    try {
        if (!userService.hasRole(token, "Instructor") && !userService.hasRole(token, "Admin")) {
            return ResponseEntity.status(403).body(null); 
        }
        List<user> enrolledStudents = courseService.viewEnrolledStudents(token, courseId);
        if (enrolledStudents.isEmpty()) {
            return ResponseEntity.status(204).body(enrolledStudents); 
        }
        return ResponseEntity.ok(enrolledStudents);

    } catch (RuntimeException e) {
       
        return ResponseEntity.status(400).body(null);
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


    // ================================
    // Helper Methods
    // ================================

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }
 
}
