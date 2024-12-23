package com.example.demo.controller;

import java.util.List;

import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Lesson;
import com.example.demo.services.LessonService;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private UserService userService; // Inject the UserService for role checking

    // Create a lesson
    @PostMapping("/course/{courseId}")
    public ResponseEntity<Lesson> createLesson(@PathVariable Long courseId, @RequestBody Lesson lesson) {
        Lesson createdLesson = lessonService.createLesson(lesson, courseId);
        return ResponseEntity.status(201).body(createdLesson);
    }
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }
    // Get all lessons
    @GetMapping
    public List<Lesson> getAllLessons() {
        return lessonService.getAllLessons();
    }
    // Get lessons by course ID (Instructor only)
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Lesson>> getLessonsByCourseId(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long courseId) {
        try {
            String token = extractToken(authorizationHeader); // Extract token
            if (!userService.hasRole(token, "Instructor")) { // Check if role is Instructor
                return ResponseEntity.status(403).body(null);
            }
            List<Lesson> lessons = lessonService.getLessonsByCourseId(courseId);
            return ResponseEntity.ok(lessons);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Get lesson by ID (Instructor only)
    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {
        try {
            String token = extractToken(authorizationHeader); // Extract token
            if (!userService.hasRole(token, "Instructor")) { // Check if role is Instructor
                return ResponseEntity.status(403).body("Access denied. Only instructors can access this resource.");
            }
            return lessonService.getLessonById(id)
                    .map(ResponseEntity::ok).orElse(ResponseEntity.status(404).body(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid authorization header.");
        }
    }

    // Update lesson (Instructor only)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @RequestBody Lesson updatedLesson) {
        try {
            String token = extractToken(authorizationHeader); // Extract token
            if (!userService.hasRole(token, "Instructor")) { // Check if role is Instructor
                return ResponseEntity.status(403).body("Access denied. Only instructors can update lessons.");
            }
            Lesson lesson = lessonService.updateLesson(id, updatedLesson);
            return ResponseEntity.ok(lesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid authorization header.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    // Delete lesson (Instructor only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {
        try {
            String token = extractToken(authorizationHeader); // Extract token
            if (!userService.hasRole(token, "Instructor")) { // Check if role is Instructor
                return ResponseEntity.status(403).body("Access denied. Only instructors can delete lessons.");
            }
            lessonService.deleteLesson(id);
            return ResponseEntity.ok("Lesson deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid authorization header.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
}