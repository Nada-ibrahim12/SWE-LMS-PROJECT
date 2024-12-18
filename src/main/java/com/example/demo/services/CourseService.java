package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.QuestionBank;
import com.example.demo.model.user;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.QuestionBankRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

@Service
public class CourseService {
    private JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private UserService userService; 

    public Course createCourse(Course course, String token) {
        if (!userService.hasRole(token, "Instructor")) {
            throw new RuntimeException("Only instructors can create courses");
        }
        return courseRepository.save(course);
    }

    public Course addLessonToCourse(Long courseId, Lesson lesson, String token) {
        if (!userService.hasRole(token, "Instructor")) {
            throw new RuntimeException("Only instructors can add lessons to courses");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        lesson.setCourse(course);
        course.addLesson(lesson);
        return courseRepository.save(course);
    }

    // public Course enrollStudent(Long courseId, user student) {
    //     Course course = courseRepository.findById(courseId)
    //             .orElseThrow(() -> new RuntimeException("Course not found"));
    //     course.addStudent(student);
    //     return courseRepository.save(course);
    // }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<user> viewEnrolledStudents(String token, Long courseId) {
   
        String username = jwtUtil.extractUsername(token);
    
        user currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    
        if (!currentUser.getRole().contains("ADMIN") && !currentUser.getRole().contains("INSTRUCTOR")) {
            throw new AccessDeniedException("You are not authorized to view the enrolled students.");
        }
   
        return new ArrayList<>(course.getEnrolledStudents());
    }
    
    public List<Course> viewAvailableCourses(String token) {

        String username = jwtUtil.extractUsername(token);
        user user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().contains("Student")) {
            throw new RuntimeException("Only students can view available courses.");
        }

        return courseRepository.findAll();
    }
    public QuestionBank createQuestionBank(Long courseId, QuestionBank questionBank, String token) {
        if (!userService.hasRole(token, "Instructor")) {
            throw new RuntimeException("Only instructors can create a question bank.");
        }

        // Find the course by its ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Associate the question bank with the course
        questionBank.setCourse(course);

        // Save and return the new question bank
        return questionBankRepository.save(questionBank);
    }


}

