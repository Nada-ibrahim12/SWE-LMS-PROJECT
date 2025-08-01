# Learning Management System (LMS) - API Documentation

## Overview

This project is a **Learning Management System (LMS)** backend API built with **Spring Boot**.
It provides functionality for:

* User authentication
* Course management
* Assignment submission
* Attendance tracking
* Notifications

The system supports **three main user roles**:

* Admin
* Instructor
* Student

---

## API Endpoints

### Authentication

| Method | Endpoint       | Description                                 |
| ------ | -------------- | ------------------------------------------- |
| `POST` | `/api/login`   | Authenticate a user and receive a JWT token |
| `POST` | `/api/logout`  | Invalidate the current user's token         |
| `GET`  | `/api/profile` | View user profile                           |
| `PUT`  | `/api/profile` | Update user profile                         |

---

### Admin Endpoints

| Method   | Endpoint                     | Description                 |
| -------- | ---------------------------- | --------------------------- |
| `POST`   | `/api/admin/register-user`   | Register a new user         |
| `GET`    | `/api/admin/all-users`       | Get all users               |
| `DELETE` | `/api/admin/delete-user`     | Delete a user               |
| `GET`    | `/api/admin/generate-report` | Generate performance report |
| `GET`    | `/notifications/admin/all`   | Get all notifications       |
| `POST`   | `/notifications/admin/send`  | Send a notification         |

---

### Instructor Endpoints

| Method   | Endpoint                                     | Description                     |
| -------- | -------------------------------------------- | ------------------------------- |
| `POST`   | `/api/instructor/create-course`              | Create a new course             |
| `POST`   | `/api/instructor/{courseId}/create-QB`       | Create a question bank          |
| `POST`   | `/api/instructor/{courseId}/upload-media`    | Upload media files for a course |
| `POST`   | `/assignments/instructor/create-assignment`  | Create an assignment            |
| `GET`    | `/assignments/instructor/all`                | Get all assignments             |
| `GET`    | `/assignments/instructor/{id}`               | Get assignment by ID            |
| `GET`    | `/assignments/instructor/submissions/all`    | Get all assignment submissions  |
| `GET`    | `/assignments/instructor/submissions/{id}`   | Get submission by ID            |
| `PUT`    | `/assignments/grade`                         | Grade an assignment             |
| `POST`   | `/attendance/instructor/generate-otp`        | Generate OTP for attendance     |
| `GET`    | `/attendance/instructor/all`                 | Get all attendance records      |
| `POST`   | `/quizzes/instructor/{courseId}/create-quiz` | Create a quiz                   |
| `GET`    | `/quizzes/instructor/all`                    | Get all quizzes                 |
| `GET`    | `/quizzes/instructor/submissions/all`        | Get all quiz submissions        |
| `DELETE` | `/quizzes/instructor/delete-quiz/{id}`       | Delete a quiz                   |

---

### Student Endpoints

| Method | Endpoint                                 | Description               |
| ------ | ---------------------------------------- | ------------------------- |
| `POST` | `/api/student/{courseId}/enroll`         | Enroll in a course        |
| `POST` | `/assignments/student/submit-assignment` | Submit an assignment      |
| `POST` | `/attendance/student/mark`               | Mark attendance           |
| `GET`  | `/quizzes/student/get-quiz/{id}`         | Get quiz by ID            |
| `GET`  | `/quizzes/student/get-submission/{id}`   | Get quiz submission by ID |
| `POST` | `/quizzes/submitQuiz/{quizId}`           | Submit a quiz             |

---

### Common Endpoints

| Method | Endpoint                                    | Description               |
| ------ | ------------------------------------------- | ------------------------- |
| `GET`  | `/api/courses`                              | Get all courses           |
| `GET`  | `/api/courses/{id}`                         | Get course by ID          |
| `GET`  | `/api/courses/available`                    | View available courses    |
| `GET`  | `/lessons`                                  | Get all lessons           |
| `GET`  | `/lessons/course/{courseId}`                | Get lessons by course ID  |
| `GET`  | `/lessons/{lessonId}`                       | Get lesson by ID          |
| `GET`  | `/notifications/user/{userId}`              | Get user notifications    |
| `GET`  | `/notifications/user/{userId}/unread`       | Get unread notifications  |
| `PUT`  | `/notifications/mark-read/{notificationId}` | Mark notification as read |

---

## Authentication Requirements

All endpoints (except `/api/login`) require a valid JWT token in the `Authorization` header:

```
Authorization: Bearer <token>
```

---

## Project Structure

The project contains the following main packages:

* `controller` — handles API endpoints
* `dto` — Data Transfer Objects for input/output
* `model` — defines core data structures
* `repository` — manages data storage in memory (or mocks, no database)
* `security` — handles JWT authentication and security configuration
* `services` — contains business logic and operations

---

## Models

Key models include:

* User (Admin, Instructor, Student)
* Course
* Lesson
* Assignment
* AssignmentSubmission
* Quiz
* QuizSubmission
* Question
* QuestionBank
* Attendance
* Notification

---

## Testing

The project includes:

* Unit tests
* Integration tests

All APIs were tested manually using **Postman** to ensure correct behavior.

---

## Setup Instructions

1. Clone the repository
2. Open the project in your preferred IDE
3. Build the project with **Maven**
4. Run the **Spring Boot** application

Note: This project does **not** connect to an external database. Data is stored in memory or mocked for demonstration purposes.

---

## Technologies Used

* Java 17
* Spring Boot
* Spring Security
* JWT for authentication
* Maven for build and dependency management
* Postman for manual API testing

---

## Error Handling

The API uses clear and standard HTTP status codes:

| Status Code                 | Meaning                           |
| --------------------------- | --------------------------------- |
| `200 OK`                    | Successful request                |
| `400 Bad Request`           | Invalid input                     |
| `401 Unauthorized`          | Missing or invalid authentication |
| `403 Forbidden`             | Access denied                     |
| `404 Not Found`             | Resource does not exist           |
| `500 Internal Server Error` | Unexpected server error           |

Each error response returns a message describing the issue.

