package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.QuizSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<QuizSubmission> submitQuiz(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("quizId") Long quizId,
            @RequestBody QuizSubmission quizSubmission) {

        String token = extractToken(authorizationHeader);
        if (!userService.hasRole(token, "Student")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            quizSubmission.setQuiz(quizService.getQuizById(quizId));
            QuizSubmission savedSubmission = quizService.submitQuiz(quizSubmission);
            if (savedSubmission.isRequiresManualGrading()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(savedSubmission);
            }
            return ResponseEntity.ok(savedSubmission);
        } catch (RuntimeException ex) {
            System.out.println("Error during quiz submission: " + ex.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

//    @PostMapping("/gradeSubmission/{submissionId}")
//    public ResponseEntity<QuizSubmission> gradeSubmission(
//            @RequestHeader("Authorization") String authorizationHeader,
//            @RequestBody List<Answer> gradedAnswers,
//            @PathVariable Long submissionId) {
//
//        String token = extractToken(authorizationHeader);
//        if (!userService.hasRole(token, "Instructor")) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
//
//        QuizSubmission submission = quizService.findQuizSubmissionById(submissionId);
//
//        int additionalScore = 0;
//        for (Answer gradedAnswer : gradedAnswers) {
//            Answer originalAnswer = quizService.findAnswerById(gradedAnswer.getId())
//                    .orElseThrow(() -> new RuntimeException("Answer not found."));
//            originalAnswer.setScore(gradedAnswer.getScore());
//            additionalScore += gradedAnswer.getScore();
//        }
//
//        submission.setScore(submission.getScore() + additionalScore);
//        submission.setRequiresManualGrading(false);  // All questions graded
//        QuizSubmission updatedSubmission = quizService.updateQuizSubmission(submission);
//
//        return ResponseEntity.ok(updatedSubmission);
//    }
}
