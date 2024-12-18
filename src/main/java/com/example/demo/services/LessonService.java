package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Create a new lesson
    public Lesson createLesson(Lesson lesson, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    // Get all lessons
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    // Get lessons by course ID
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getLessons();
    }

    // Get a lesson by ID
    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    // Update a lesson
    public Lesson updateLesson(Long lessonId, Lesson updatedLesson) {
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        existingLesson.setTitle(updatedLesson.getTitle());
        existingLesson.setContent(updatedLesson.getContent());
        return lessonRepository.save(existingLesson);
    }

    // Delete a lesson
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    public boolean isLessonInCourse(Long lessonId, Long courseId) {
        return lessonRepository.existsByIdAndCourseId(lessonId, courseId);
    }
}

