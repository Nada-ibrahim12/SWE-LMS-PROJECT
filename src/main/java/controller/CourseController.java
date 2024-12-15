package controller;

import model.Course;
import service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<String> createCourse(@RequestBody Course course) {
        courseService.createCourse(course);
        return ResponseEntity.ok("Course created successfully!");
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
}