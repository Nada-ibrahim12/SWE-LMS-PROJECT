/*Performance Tracking & Bonus Features
Tasks:

Implement PerformanceTracking module:
Develop methods to track quiz scores, assignment submissions, and attendance.
Create APIs for instructors and admins to fetch progress reports.
Implement bonus features:
Generate Excel reports for performance data using a library like Apache POI.
Create visual progress reports (charts) using a library like Chart.js (for frontend or integration).
Handle email notifications for important events (graded assignments, enrollments).
Write unit tests for performance tracking logic. */
package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class PerformanceTrackingController {

}

/*[11:26 am, 15/12/2024] Nada Ibrahim: Hegazy

User Management
Tasks:

Implement User entity with JPA annotations for roles (Admin, Instructor, Student).
Create UserRepository for database operations.
Develop UserService for user registration, login, and profile management.
Create REST endpoints in UserController for:
Registering users with role-based access.
Updating and fetching user profiles.
Configure Spring Security for authentication and role-based authorization.
Write unit tests for UserService using JUnit.
[11:26 am, 15/12/2024] Nada Ibrahim: Menna

Course Management
Tasks:

Implement Course and Lesson entities with JPA annotations.
Develop CourseRepository and LessonRepository.
Implement CourseService for:
Creating courses (title, description, etc.).
Adding lessons to courses.
Handling course enrollment (students enrolling in courses).
Create REST endpoints in CourseController for:
Course creation and lesson addition (for instructors).
Viewing available courses and enrolling (for students).
Viewing enrolled students (for instructors and admins).
Write unit tests for CourseService.
[11:26 am, 15/12/2024] Nada Ibrahim: Mai

Assessment & Grading
Tasks:

Implement Assessment (Quiz & Assignment) and Question entities.
Create AssessmentRepository and QuestionRepository.
Implement AssessmentService for:
Quiz creation (with MCQ, true/false, short answers).
Randomized question selection for quiz attempts.
Assignment submission by students.
Grading assignments and quizzes.
Create REST endpoints in AssessmentController for:
Quiz and assignment creation.
Submitting assignments.
Viewing grades (for students).
Write unit tests for AssessmentService.
[11:26 am, 15/12/2024] Nada Ibrahim: Nada

Attendance & Notifications
Tasks:

Implement Attendance and Notification entities with JPA annotations.
Create AttendanceRepository and NotificationRepository.
Implement AttendanceService for:
Generating OTPs for lessons (for instructors).
Students entering OTPs to mark attendance.
Tracking attendance for each course.
Implement NotificationService for:
Sending notifications for enrollments, grades, and updates.
Managing unread/read notifications for users.
Create REST endpoints in AttendanceController and NotificationController.
Write unit tests for both services.
[11:26 am, 15/12/2024] Nada Ibrahim: George

Performance Tracking & Bonus Features
Tasks:

Implement PerformanceTracking module:
Develop methods to track quiz scores, assignment submissions, and attendance.
Create APIs for instructors and admins to fetch progress reports.
Implement bonus features:
Generate Excel reports for performance data using a library like Apache POI.
Create visual progress reports (charts) using a library like Chart.js (for frontend or integration).
Handle email notifications for important events (graded assignments, enrollments).
Write unit tests for performance tracking logic.
[11:26 am, 15/12/2024] Nada Ibrahim: Gma3a boso de El t2seema howa hegazy wakhed El user management ay 7ad wakhed ay 7aga Tanya w 3aiz ybdlha okay howa bs t2seema keda b7es nengz w nebd2 y3nii
[11:27 am, 15/12/2024] Nada Ibrahim: F shofo keda */
