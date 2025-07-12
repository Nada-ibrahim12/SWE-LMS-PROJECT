package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.services.CourseService;
import com.example.demo.services.LessonService;
import com.example.demo.services.UserService;
@RestController
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }


    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    // Get lessons by course ID (Instructor only)
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getLessonsByCourseId(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long courseId) {
        try {
            String token = extractToken(authorizationHeader);
            if (!userService.hasRole(token, "Instructor")||!userService.hasRole(token, "Student")) {
                return ResponseEntity.status(403).body("Access denied.");
            }
            List<Lesson> lessons = lessonService.getLessonsByCourseId(courseId);
            return ResponseEntity.ok(lessons);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    @GetMapping("/{lessonId}")
    public ResponseEntity<?> getLessonById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long lessonId) {
        try {
            String token = extractToken(authorizationHeader);
            if (!userService.hasRole(token, "Instructor")||!userService.hasRole(token, "Student")) {
                return ResponseEntity.status(403).body("Access denied.");
            }
            return lessonService.getLessonById(lessonId)
                    .map(ResponseEntity::ok).orElse(ResponseEntity.status(404).body(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    @PutMapping("/{courseId}/lesson/{id}")
    public ResponseEntity<?> updateLesson(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long courseId,
            @PathVariable Long id,
            @RequestBody Lesson updatedLesson) {
        try {
            String token = extractToken(authorizationHeader);
            if (!userService.hasRole(token, "Instructor")) {
                return ResponseEntity.status(403).body("Access denied. Only instructors can update lessons.");
            }
            if (!lessonService.isLessonInCourse(id, courseId)) {
                return ResponseEntity.status(404).body("Lesson not found in the specified course.");
            }
            Lesson lesson = lessonService.updateLesson(id, updatedLesson);
            return ResponseEntity.ok(lesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }


    @DeleteMapping("/{courseId}/lesson/{id}")
    public ResponseEntity<?> deleteLesson(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long courseId,
            @PathVariable Long id) {
        try {
            String token = extractToken(authorizationHeader);
            if (!userService.hasRole(token, "Instructor")) {
                return ResponseEntity.status(403).body("Access denied. Only instructors can delete lessons.");
            }
            if (!lessonService.isLessonInCourse(id, courseId)) {
                return ResponseEntity.status(404).body("Lesson not found in the specified course.");
            }
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
            Lesson lesson = lessonRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + id));
            course.getLessons().remove(lesson);
            courseRepository.save(course);
            lessonService.deleteLesson(id);

            return ResponseEntity.ok("Lesson deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }


}
