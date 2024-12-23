package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AttendanceRequest;
import com.example.demo.model.Attendance;
import com.example.demo.services.AttendanceService;
import com.example.demo.services.CourseService;
import com.example.demo.services.LessonService;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LessonService lessonService;

    private String extractToken(String authorizationHeader) {
        if (isValidAuthorizationHeader(authorizationHeader)) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    @PostMapping("/student/mark")
    public ResponseEntity<String> markAttendance(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody AttendanceRequest request) {
        try {
            System.out.println("Authorization Header: " + authorizationHeader);

            String token = extractToken(authorizationHeader);
            System.out.println("Extracted Token: " + token);

            if (userService.hasRole(token, "Student")) {
                System.out.println("Student Role Validated");
                attendanceService.markAttendance(request);
                return ResponseEntity.ok("Attendance marked successfully");
            }
            System.out.println("Unauthorized Access Attempt");
            return ResponseEntity.status(403).body("Unauthorized");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Request: " + e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Runtime Exception: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/instructor/generate-otp")
    public ResponseEntity<String> generateOtp(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Long courseId,
            @RequestParam Long lessonId) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                if (courseService.getCourseById(courseId) == null) {
                    return ResponseEntity.status(404).body("Course not found");
                }

//                if (!lessonService.isLessonInCourse(lessonId, courseId)) {
//                    return ResponseEntity.status(400).body("Lesson does not belong to the specified course");
//                }
                String otp = attendanceService.generateAndStoreOtp(courseId, lessonId);
                return ResponseEntity.ok("Generated OTP: " + otp);
            }
            return ResponseEntity.status(403).body("Forbidden");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
//    private OTPService otpService;
//
//    @GetMapping("/generateOtp")

    @GetMapping("/instructor/all")
    public ResponseEntity<List<Attendance>> getAllAttendance(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                return ResponseEntity.ok(attendanceService.findAll());
            }
            return ResponseEntity.status(403).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/instructor/attendance/{id}")
    public ResponseEntity<Optional<Attendance>>    getAttendanceById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                return ResponseEntity.ok(attendanceService.findById(id));
            }
            return ResponseEntity.status(403).body(Optional.empty());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Optional.empty());
        }
    }

    @GetMapping("/instructor/attendance/{status}")
    public ResponseEntity<Optional<Attendance>> getByAttendanceStatus(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Boolean status) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                return ResponseEntity.ok(attendanceService.findByStatus(status));
            }
            return ResponseEntity.status(403).body(Optional.empty());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Optional.empty());
        }
    }

//    @PostMapping("/add")
//    public Attendance addAttendance(@RequestBody Attendance attendance) {
//        return attendanceService.save(attendance);
//    }

//    @DeleteMapping("/{id}")
//    public void deleteAttendanceById(@PathVariable Long id) {
//        attendanceService.deleteById(id);
//    }

    @GetMapping("/instructor/student/{studentId}")
    public ResponseEntity<Optional<Attendance>> findAttendanceOfStudent(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long studentId) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                return ResponseEntity.ok(attendanceService.findAttendanceOfStudent(studentId));
            }
            return ResponseEntity.status(403).body(Optional.empty());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Optional.empty());
        }
    }

    @GetMapping("/instructor/lesson/{lessonId}")
    public ResponseEntity<Optional<Attendance>> findAllAttendanceOfLesson(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long lessonId) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Instructor")) {
                return ResponseEntity.ok(attendanceService.findAllAttendanceOfLesson(lessonId));
            }
            return ResponseEntity.status(403).body(Optional.empty());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Optional.empty());
        }
    }
    // @GetMapping("/instructor/track/student/{studentId}/lesson/{lessonId}")
    // public ResponseEntity<Object> getAttendancePrograss(
    //         @RequestHeader("Authorization") String authorizationHeader,
    //         @PathVariable Long studentId,
    //         @PathVariable Long lessonId) {
    //     try {
    //         String token = extractToken(authorizationHeader);
    //         if (userService.hasRole(token, "Instructor")) {
    //             Optional<Attendance> attendance = attendanceService.getAttendanceForStudentInLesson(studentId, lessonId);
    //             if (attendance.isPresent()) {
    //                 return ResponseEntity.ok(attendance);
    //             } else {
    //                 return ResponseEntity.status(404).body("Not attend.");
    //             }
    //         }
    //         return ResponseEntity.status(403).body(Optional.empty());
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.status(400).body("Invalid request.");
    //     }
    // }
    

}
