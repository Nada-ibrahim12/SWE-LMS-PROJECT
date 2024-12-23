package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Course;

@Repository
public class CourseRepository {

    private final List<Course> courseList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1); // AtomicLong for thread-safe auto-incremented IDs

    // Get all courses
    public List<Course> findAll() {
        return new ArrayList<>(courseList);
    }

    // Find course by ID
    public Optional<Course> findById(Long id) {
        return courseList.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst();
    }

    // Save a new course
    public Course save(Course course) {
        // Check if the course already exists (by ID)
        Optional<Course> existingCourse = findById(course.getId());

        if (existingCourse.isPresent()) {
            // Update the existing course (e.g., adding lessons)
            Course existing = existingCourse.get();
            existing.setLessons(course.getLessons()); // Update lessons or other fields as needed
            return existing;
        }

        // Validate duplicate title only for new courses
        if (isDuplicateTitle(course.getTitle())) {
            throw new IllegalArgumentException("Course with the same title already exists: " + course.getTitle());
        }

        // Save new course with auto-incremented ID
        course.setId(idGenerator.getAndIncrement());
        courseList.add(course);
        return course;
    }

    // Delete a course by ID
    public void deleteById(Long id) {
        courseList.removeIf(course -> course.getId().equals(id));
    }

    // Find course by title
    public Optional<Course> findByTitle(String title) {
        return courseList.stream()
                .filter(course -> course.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    // Check for duplicate course title
    private boolean isDuplicateTitle(String title) {
        return courseList.stream()
                .anyMatch(course -> course.getTitle().equalsIgnoreCase(title));
    }
}