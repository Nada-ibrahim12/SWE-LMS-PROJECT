package org.example.learning_managment_system;
import com.example.demo.model.Course;
import com.example.demo.model.Student;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashMap;

class StudentTest {

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new Student("1", "JohnDoe", "password123", "Student", "john@example.com");
        course = new Course();
        course.setId(1L);
        course.setTitle("Java Programming");
    }

    @Test
    void testAddEnrolledCourse() {
        student.addEnrolledCourse(course.getTitle());
        assertEquals(1, student.getEnrolledCourses().size());
        assertEquals("Java Programming", student.getEnrolledCourses().get(0));
    }

    @Test
    void testGetStudentEmail() {
        student.setStudentEmail("student@example.com");
        assertNotEquals("student@example.com", student.getStudentEmail());
    }

    @Test
    void testAttendanceList() {
        student.getAttendanceList().put(course.getId(), true);
        assertEquals(1, student.getAttendanceList().size());
        assertTrue(student.getAttendanceList().get(course.getId()));
    }
}

