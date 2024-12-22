package com.example.demo.controller;

import java.util.List;

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
import com.example.demo.model.QuizSubmission;
import com.example.demo.services.QuizService;
import com.example.demo.services.SubmitQuizService;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/quizzes")
public class QuizController {
    @Autowired
    private SubmitQuizService submitQuizService; // Updated service name

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    // Helper method to extract token
    private String extractToken(String authorizationHeader) {
        if (isValidAuthorizationHeader(authorizationHeader)) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    // Create quiz (Instructor only)
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

    // Get all quizzes (Instructor only)
    @GetMapping("/instructor/all")
    public ResponseEntity<List<Quiz>> getAllQuizzes(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Instructor")) {
            return ResponseEntity.ok(quizService.getAllQuizzes());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Get quiz by ID (Student only)
    @GetMapping("/student/get-quiz/{id}")
    public ResponseEntity<Quiz> getQuizById(@RequestHeader("Authorization") String authorizationHeader,
                                            @PathVariable Long id) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Student")) {
            return ResponseEntity.ok(quizService.getQuizById(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Delete quiz by ID (Instructor only)
    @DeleteMapping("/instructor/delete-quiz/{id}")
    public ResponseEntity<Void> deleteQuiz(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Long id) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Instructor")) {
            quizService.deleteQuiz(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Randomize quiz questions (Instructor only)
    @GetMapping("/instructor/quizzes-randomize/{quizId}")
    public ResponseEntity<List<Question>> getRandomQuestionsForAttempt(@RequestHeader("Authorization") String authorizationHeader,
                                                                        @PathVariable Long quizId,
                                                                        @RequestParam int numQuestions) {
        String token = extractToken(authorizationHeader);
        if (userService.hasRole(token, "Instructor")) {
            return ResponseEntity.ok(quizService.getRandomizedQuestions(quizId, numQuestions));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/submitQuiz/{quizId}")
    public ResponseEntity<QuizSubmission> submitQuiz(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("quizId") Long quizId,@RequestBody QuizSubmission quizSubmission) {

    String token = extractToken(authorizationHeader);
        if (!userService.hasRole(token, "Student")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            QuizSubmission savedSubmission = submitQuizService.submitQuiz(quizSubmission);
            return ResponseEntity.ok(savedSubmission);
        } catch (RuntimeException ex) {
            System.out.println("Error during quiz submission: " + ex.getMessage());
        return ResponseEntity.badRequest().body(null);
        }
    }

    // @PostMapping("/assignments/{submissionId}/grade")
    // public ResponseEntity<String> gradeAssignment(@RequestHeader("Authorization") String authorizationHeader,
    //                                               @PathVariable Long submissionId,
    //                                               @RequestParam int grade,
    //                                               @RequestParam String feedback) {
    //     String token = extractToken(authorizationHeader);
    //     if (userService.hasRole(token, "Instructor")) {
    //         submitQuizService.gradeAssignment(submissionId, grade, feedback);
    //         return ResponseEntity.ok("Assignment graded successfully.");
    //     }
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    // }

    // Evaluate a quiz
    // @PostMapping("/quizzes/{quizId}/evaluate")
    // public ResponseEntity<QuizResult> evaluateQuiz(@RequestBody Map<Long, String> answers,
    //                                                @PathVariable Long quizId) {
    //     QuizResult result = submitQuizService.evaluateQuiz(quizId, answers);
    //     return ResponseEntity.ok(result);
    // }
}
