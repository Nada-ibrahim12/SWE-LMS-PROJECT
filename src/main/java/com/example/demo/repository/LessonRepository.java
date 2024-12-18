package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Lesson;

@Repository
public class LessonRepository {
    private final List<Lesson> lessonList = new ArrayList<>();

    public List<Lesson> findAll() {
        return new ArrayList<>(lessonList);
    }

    public Optional<Lesson> findById(Long id) {
        return lessonList.stream()
                .filter(lesson -> lesson.getId().equals(id))
                .findFirst();
    }

    public Lesson save(Lesson lesson) {
        lessonList.add(lesson);
        return lesson;
    }

    public void deleteById(Long id) {
        lessonList.removeIf(lesson -> lesson.getId().equals(id));
    }

    public Optional<Lesson> findByTitle(String title) {
        return lessonList.stream()
                .filter(lesson -> lesson.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

}

