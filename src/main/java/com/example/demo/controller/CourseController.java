package com.example.demo.controller;// package com.example.demo.controller;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.demo.model.Course;
// import com.example.demo.model.Lesson;
// import com.example.demo.model.user;
// import com.example.demo.services.CourseService;
// import com.example.demo.services.EnrollmentService;
// import com.example.demo.services.UserService;

// @RestController
// @RequestMapping("/api/courses")
// public class CourseController {

//     @Autowired
//     private CourseService courseService;
    
//     private  EnrollmentService enrollmentService;
   
//    @Autowired
//     private UserService userService;

//     @PostMapping("/instructor/create-course")
//     public ResponseEntity<Course> createCourse(@RequestBody Course course, @RequestHeader("Authorization") String token) {
//         if (!userService.hasRole(token, "Instructor")) {
//             return ResponseEntity.status(403).body(null); 
//         }
//         Course createdCourse = courseService.createCourse(course, token);
//         return ResponseEntity.ok(createdCourse);
//     }
//     @PostMapping("/{courseId}/lessons")
//     public ResponseEntity<Course> addLessonToCourse(@PathVariable Long courseId, @RequestBody Lesson lesson,
//                                                     @RequestHeader("Authorization") String token) {
//         if (!userService.hasRole(token, "Instructor")) {
//             return ResponseEntity.status(403).body(null); 
//         }
//         Course updatedCourse = courseService.addLessonToCourse(courseId, lesson, token);
//         return ResponseEntity.ok(updatedCourse);
//     }

//     @PostMapping("/{courseId}/enroll")
//     public ResponseEntity<Course> enrollStudent(@PathVariable Long courseId, @RequestBody user student) {
//         Course updatedCourse = courseService.enrollStudent(courseId, student);
//         return ResponseEntity.ok(updatedCourse);
//     }

//     @GetMapping
//     public List<Course> getAllCourses() {
//         return courseService.getAllCourses();
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
//         return courseService.getCourseById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//         @GetMapping("/{courseId}/enrolled")
//     public ResponseEntity<List<String>> viewEnrolledStudents(
//             @RequestHeader("Authorization") String token, 
//             @PathVariable Long courseId) {
//         try {
        
//         List<user> enrolledStudents = courseService.viewEnrolledStudents(token, courseId);

//         List<String> usernames = enrolledStudents.stream()
//                 .map(user::getUsername)  
//                 .collect(Collectors.toList());

//         return ResponseEntity.ok(usernames);
//     } catch (RuntimeException e) {
        
//         return ResponseEntity.status(403).body(List.of(e.getMessage()));
//     }
// }

//     @GetMapping("/availableCourses")
//     public ResponseEntity<List<Course>> viewAvailableCourses(@RequestHeader("Authorization") String token) {
//         try {
//             List<Course> availableCourses = courseService.viewAvailableCourses(token);
//             return ResponseEntity.ok(availableCourses);
//         } catch (RuntimeException e) {
//             return ResponseEntity.status(403).body(null);
//         }
//     }

    
// }