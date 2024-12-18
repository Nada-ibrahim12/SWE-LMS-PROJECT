package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Course;

@Repository
public class CourseRepository {
    private final List<Course> courseList = new ArrayList<>();

    public List<Course> findAll() {
        return new ArrayList<>(courseList);
    }

    public Optional<Course> findById(Long id) {
        return courseList.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst();
    }

    public Course save(Course course) {
        courseList.add(course);
        return course;
    }

    public void deleteById(Long id) {
        courseList.removeIf(course -> course.getId().equals(id));
    }

    public Optional<Course> findByTitle(String title) {
        return courseList.stream()
                .filter(course -> course.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }
   

}

