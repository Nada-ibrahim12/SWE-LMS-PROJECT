package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Course;
import com.example.demo.model.user;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

@Service
public class EnrollmentService {

    private JwtUtil jwtUtil = new JwtUtil();

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public EnrollmentService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }
    @Autowired
    private UserService userService; 
    
    public void enrollInCourse(String token, Long courseId) {
        
        String username = jwtUtil.extractUsername(token);

        user student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        if (!student.getRole().contains("Student")) {
            throw new RuntimeException("Only students can enroll in courses.");
        }

        Course course = courseRepository.findById(Long.valueOf(courseId))
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (course.getEnrolledStudents().contains(student)) {
            throw new RuntimeException("Student already enrolled in this course.");
        }

        course.addStudent(student);

        courseRepository.save(course);

        System.out.println("Student " + student.getUsername() + " enrolled in course: " + course.getTitle());
    }
   
     
}


