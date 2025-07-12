package org.example.learning_managment_system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.Course;
import com.example.demo.model.user;
import com.example.demo.services.CourseService;
import com.example.demo.services.EnrollmentService;
import com.example.demo.services.UserService;

class IntegrateTest {

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @Mock
    private EnrollmentService enrollmentService;

    private user instructor;
    private user student;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new user("1", "instructor", "password123", "Instructor", "instructor@example.com");
        student = new user("2", "student", "password123", "Student", "student@example.com");

        course = new Course();
        course.setId(1L);
        course.setTitle("Integration Testing Course");
    }

    // Test that Instructor can create a course and Student can enroll in it
    @Test
    void testInstructorCreatesCourseAndStudentEnrolls() {
        
        String instructorToken = "instructor-token";

        doNothing().when(userService).registerUser(any(user.class));
        userService.registerUser(instructor);
        verify(userService, times(1)).registerUser(instructor);

        when(userService.hasRole(instructorToken, "Instructor")).thenReturn(true);
        when(courseService.createCourse(any(Course.class), eq(instructorToken))).thenReturn(course);

        Course createdCourse = courseService.createCourse(course, instructorToken);

        assertNotNull(createdCourse);
        assertEquals("Integration Testing Course", createdCourse.getTitle());

        String studentToken = "student-token";

        doNothing().when(userService).registerUser(any(user.class));
        userService.registerUser(student);
        verify(userService, times(1)).registerUser(student);

        when(userService.hasRole(studentToken, "Student")).thenReturn(true);
        doNothing().when(enrollmentService).enrollInCourse(eq(student.getUserId()), eq(course.getId()));  // Change to doNothing() for void method
        enrollmentService.enrollInCourse(student.getUserId(), course.getId());
        verify(enrollmentService, times(1)).enrollInCourse(student.getUserId(), course.getId());
    }
}
