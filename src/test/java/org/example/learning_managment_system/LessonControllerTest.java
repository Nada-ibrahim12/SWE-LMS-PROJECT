package org.example.learning_managment_system;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.services.LessonService;

class LessonControllerTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LessonService lessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLessons() {
        // Arrange
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setTitle("Lesson 1");

        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setTitle("Lesson 2");

        when(lessonRepository.findAll()).thenReturn(Arrays.asList(lesson1, lesson2));

        List<Lesson> lessons = lessonService.getAllLessons();
        assertEquals(2, lessons.size());
        verify(lessonRepository, times(1)).findAll();
    }

    @Test
    void testGetLessonsByCourseId() {
    
        Course course = new Course();
        course.setId(1L);

        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setTitle("Lesson 1");
        lesson1.setCourse(course);

        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setTitle("Lesson 2");
        lesson2.setCourse(course);

        course.setLessons(Arrays.asList(lesson1, lesson2));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        List<Lesson> lessons = lessonService.getLessonsByCourseId(1L);
        assertEquals(2, lessons.size());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLessonById() {
        
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTitle("Lesson 1");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        Optional<Lesson> foundLesson = lessonService.getLessonById(1L);

        assertTrue(foundLesson.isPresent());
        assertEquals("Lesson 1", foundLesson.get().getTitle());
        verify(lessonRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateLesson() {
    
        Course course = new Course();
        course.setId(1L);

        Lesson lesson = new Lesson();
        lesson.setTitle("New Lesson");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson createdLesson = lessonService.createLesson(lesson, 1L);
        assertEquals("New Lesson", createdLesson.getTitle());
        verify(courseRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void testUpdateLesson() {
        
        Lesson existingLesson = new Lesson();
        existingLesson.setId(1L);
        existingLesson.setTitle("Old Title");

        Lesson updatedLesson = new Lesson();
        updatedLesson.setTitle("Updated Title");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(existingLesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(existingLesson);

        Lesson result = lessonService.updateLesson(1L, updatedLesson);

        assertEquals("Updated Title", result.getTitle());
        verify(lessonRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(existingLesson);
    }

    @Test
    void testDeleteLesson() {
    
        lessonService.deleteLesson(1L);
        verify(lessonRepository, times(1)).deleteById(1L);
    }
}
