package org.example.learning_managment_system;
// Necessary imports
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.controller.QuizController;
import com.example.demo.model.Quiz;
import com.example.demo.model.QuizSubmission;
import com.example.demo.services.QuizService;
import com.example.demo.services.UserService;

class QuizTest {

    @Mock
    private QuizService quizService;

    @Mock
    private UserService userService;

    @InjectMocks
    private QuizController quizController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateQuiz_AsInstructor_ShouldReturnCreatedQuiz() {
        String token = "testToken";
        Long courseId = 1L;
        Quiz quiz = new Quiz();
        quiz.setId(1L);

        when(userService.hasRole(token, "Instructor")).thenReturn(true);
        when(quizService.createQuiz(courseId, quiz)).thenReturn(quiz);

        ResponseEntity<Quiz> response = quizController.createQuiz("Bearer " + token, courseId, quiz);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(quiz, response.getBody());
        verify(quizService, times(1)).createQuiz(courseId, quiz);
    }

    @Test
    void testCreateQuiz_NotInstructor_ShouldReturnUnauthorized() {
        String token = "testToken";
        Long courseId = 1L;
        Quiz quiz = new Quiz();

        when(userService.hasRole(token, "Instructor")).thenReturn(false);

        ResponseEntity<Quiz> response = quizController.createQuiz("Bearer " + token, courseId, quiz);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(quizService, never()).createQuiz(anyLong(), any());
    }

    @Test
    void testGetAllQuizzes_AsInstructor_ShouldReturnQuizzes() {
        String token = "testToken";
        List<Quiz> quizzes = Arrays.asList(new Quiz(), new Quiz());

        when(userService.hasRole(token, "Instructor")).thenReturn(true);
        when(quizService.getAllQuizzes()).thenReturn(quizzes);

        ResponseEntity<List<Quiz>> response = quizController.getAllQuizzes("Bearer " + token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(quizzes, response.getBody());
        verify(quizService, times(1)).getAllQuizzes();
    }

    @Test
    void testGetQuizById_AsStudent_ShouldReturnQuiz() {
        String token = "testToken";
        Long quizId = 1L;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        when(userService.hasRole(token, "Student")).thenReturn(true);
        when(quizService.getQuizById(quizId)).thenReturn(quiz);

        ResponseEntity<Quiz> response = quizController.getQuizById("Bearer " + token, quizId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(quiz, response.getBody());
        verify(quizService, times(1)).getQuizById(quizId);
    }

    @Test
    void testSubmitQuiz_AsStudent_ShouldReturnSubmission() {
        String token = "testToken";
        Long quizId = 1L;
        QuizSubmission submission = new QuizSubmission();
        submission.setQuiz(quizId);

        when(userService.hasRole(token, "Student")).thenReturn(true);
        when(quizService.submitQuiz(submission)).thenReturn(submission);

        ResponseEntity<QuizSubmission> response = quizController.submitQuiz("Bearer " + token, quizId, submission);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(submission, response.getBody());
        verify(quizService, times(1)).submitQuiz(submission);
    }

    @Test
    void testSubmitQuiz_NotStudent_ShouldReturnForbidden() {
        String token = "testToken";
        Long quizId = 1L;
        QuizSubmission submission = new QuizSubmission();

        when(userService.hasRole(token, "Student")).thenReturn(false);

        ResponseEntity<QuizSubmission> response = quizController.submitQuiz("Bearer " + token, quizId, submission);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(quizService, never()).submitQuiz(any());
    }
}
