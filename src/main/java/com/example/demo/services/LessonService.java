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

        // Validate duplicate title within the course
        if (lessonRepository.isDuplicateTitleWithinCourse(lesson.getTitle(), courseId)) {
            throw new IllegalArgumentException("A lesson Updated :" + lesson.getTitle());
        }

        // Save the lesson
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

    public Lesson updateLesson(Long lessonId, Lesson updatedLesson) {
        // Fetch the existing lesson by ID
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // Check if the title has changed and if the new title already exists within the course
        if (!existingLesson.getTitle().equalsIgnoreCase(updatedLesson.getTitle())) {
            Course course = existingLesson.getCourse();


        }

        // Update the lesson details
        existingLesson.setTitle(updatedLesson.getTitle());
        existingLesson.setContent(updatedLesson.getContent());

        // Save and return the updated lesson
        return lessonRepository.save(existingLesson);
    }


    // Delete a lesson
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    public boolean isLessonInCourse(Long lessonId, Long courseId) {
        return lessonRepository.existsByIdAndCourseId(lessonId, courseId);
    }
    public Lesson addLessonToCourse(Long courseId, Lesson lesson) {
        // Fetch the course from the database
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Set the course to the lesson
        lesson.setCourse(course);

        // Save the lesson to the repository
        return lessonRepository.save(lesson);
    }


}