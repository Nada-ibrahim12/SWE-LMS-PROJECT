package org.example.learning_managment_system;
import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.user;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

class CourseTest {

    private Course course;
    private Lesson lesson;
    private user student;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setTitle("Java Programming");
        course.setDescription("Learn Java from scratch");
        course.setDuration("3 Months");

        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTitle("Introduction to Java");

        student = new user("1", "JohnDoe", "password123", "Student", "john@example.com");
    }

    @Test
    void testAddLesson() {
        course.addLesson(lesson);
        assertEquals(1, course.getLessons().size());
        assertEquals("Introduction to Java", course.getLessons().get(0).getTitle());
    }

    @Test
    void testAddStudent() {
        course.addStudent(student);
        assertEquals(1, course.getEnrolledStudents().size());
        assertEquals("JohnDoe", course.getEnrolledStudents().get(0).getUsername());
    }

    @Test
    void testGetMediaFiles() {
        course.setMediaFiles(Arrays.asList("file1.mp4", "file2.mp3"));
        assertEquals(2, course.getMediaFiles().size());
        assertEquals("file1.mp4", course.getMediaFiles().get(0));
    }
}

