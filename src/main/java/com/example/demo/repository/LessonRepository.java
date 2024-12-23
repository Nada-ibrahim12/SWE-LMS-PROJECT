package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Lesson;
// LessonRepository.java
@Repository
public class LessonRepository {

    private final List<Lesson> lessonList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1); // For auto-increment IDs

    public List<Lesson> findAll() {
        return new ArrayList<>(lessonList);
    }

    public Optional<Lesson> findById(Long id) {
        return lessonList.stream()
                .filter(lesson -> lesson.getId().equals(id))
                .findFirst();
    }

    public Lesson save(Lesson lesson) {
        if (isDuplicateTitleWithinCourse(lesson.getTitle(), lesson.getCourse().getId())) {
            throw new IllegalArgumentException("Lesson with the same title already exists in this course.");
        }

        if (lessonList.stream().anyMatch(existingLesson -> existingLesson.getId().equals(lesson.getId()))) {
            throw new IllegalArgumentException("Lesson with the same ID already exists.");
        }

        lessonList.add(lesson);
        return lesson;
    }

    public void deleteById(Long id) {
        lessonList.removeIf(lesson -> lesson.getId().equals(id));
    }

    public Optional<Lesson> findByTitleAndCourseId(String title, Long courseId) {
        return lessonList.stream()
                .filter(lesson -> lesson.getTitle().equalsIgnoreCase(title) && lesson.getCourse().getId().equals(courseId))
                .findFirst();
    }

    public boolean existsByIdAndCourseId(Long lessonId, Long courseId) {
        return lessonList.stream()
                .anyMatch(lesson -> lesson.getId().equals(lessonId) && lesson.getCourse().getId().equals(courseId));
    }

    // Change this method's access modifier to public
    public boolean isDuplicateTitleWithinCourse(String title, Long courseId) {
        return lessonList.stream()
                .anyMatch(lesson -> lesson.getTitle().equalsIgnoreCase(title) && lesson.getCourse().getId().equals(courseId));
    }
}