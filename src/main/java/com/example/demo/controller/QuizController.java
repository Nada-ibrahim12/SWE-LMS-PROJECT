package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.QuizSubmission;
import com.example.demo.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Question;
import com.example.demo.model.Quiz;
import com.example.demo.services.QuizService;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    private String extractToken(String authorizationHeader) {
        if (isValidAuthorizationHeader(authorizationHeader)) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    @PostMapping("/instructor/{courseId}/create-quiz")
    public ResponseEntity<Quiz> createQuiz(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Long courseId,
                                           @RequestBody Quiz quiz) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Instructor")) {
            Quiz createdQuiz = quizService.createQuiz(courseId, quiz);
            return ResponseEntity.ok(createdQuiz);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    // @PostMapping("/instructor/generate-QB")
    // public ResponseEntity<Quiz> generateQB(@RequestHeader("Authorization") String authorizationHeader,
    //                                        @RequestBody Quiz quiz) {
    //     String token = extractToken(authorizationHeader);
    //     if (userService.hasRole(token, "Instructor")) {
    //         return ResponseEntity.ok(quizService.createQuiz(null, quiz));
    //     }
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    // }

    @GetMapping("/instructor/all")
    public ResponseEntity<List<Quiz>> getAllQuizzes(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Instructor")) {
            return ResponseEntity.ok(quizService.getAllQuizzes());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/student/get-quiz/{id}")
    public ResponseEntity<Quiz> getQuizById(@RequestHeader("Authorization") String authorizationHeader,
                                            @PathVariable Long id) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Student")) {
            return ResponseEntity.ok(quizService.getQuizById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/student/take-quiz/{id}")
    public ResponseEntity<Quiz> takeQuiz(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Student")) {
            Quiz quiz = quizService.getQuizById(id);
            if (quiz != null) {
                return ResponseEntity.ok(quiz);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/student/submit-quiz/{id}")
    public ResponseEntity<String> submitQuiz(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @RequestBody QuizSubmission submission) {
        try {
            String token = extractToken(authorizationHeader);
            if (userService.hasRole(token, "Student")) {
                String studentId = userService.getUserFromToken(token).getUserId();
                submission.setStudentId(studentId);
                String email = userService.getUserFromToken(token).getEmail();
                String message = "Quiz submitted successfully and graded\n";
                notificationService.sendNotification(studentId, "Student", message, email, true);
                return ResponseEntity.ok("Quiz submitted successfully and graded");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to submit quiz: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @DeleteMapping("/instructor/delete-guiz/{id}")
    public ResponseEntity<Void> deleteQuiz(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Long id) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Instructor")) {
            quizService.deleteQuiz(id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/instructor/quizzes-radnomize/{quizId}")
    public List<Question> getRandomQuestionsForAttempt(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestParam Long quizId, @RequestParam int numQuestions) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Instructor")) {
            return quizService.getRandomizedQuestions(quizId, numQuestions);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }
}