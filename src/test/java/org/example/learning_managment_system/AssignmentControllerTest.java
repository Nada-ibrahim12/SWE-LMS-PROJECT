package org.example.learning_managment_system;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.controller.AssignmentController;
import com.example.demo.model.Assignment;
import com.example.demo.model.AssignmentSubmission;
import com.example.demo.model.user;
import com.example.demo.services.AssignmentService;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AssignmentControllerTest {

    @InjectMocks
    private AssignmentController assignmentController;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitAssignment_ValidTokenAndStudentRole_ShouldSubmitSuccessfully() throws Exception {
        String token = "valid-token";
        String assignmentData = "{\"title\":\"Test Assignment\"}";
        MultipartFile file = mock(MultipartFile.class);
        
        when(userService.hasRole(token, "Student")).thenReturn(true);
        when(userService.getUserFromToken(token)).thenReturn(new user("1", "student123", "password123", "Student", "student@example.com"));
        when(objectMapper.readValue(assignmentData, AssignmentSubmission.class)).thenReturn(new AssignmentSubmission());

        ResponseEntity<String> response = assignmentController.submitAssignment("Bearer " + token, assignmentData, file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Assignment submitted successfully", response.getBody());
        verify(notificationService, times(1)).sendNotification(anyString(), eq("Student"), anyString(), anyString(), eq(true));
    }

    @Test
    void testGetAssignmentSubmissionById_ValidInstructorRole_ShouldReturnSubmission() {
        String token = "valid-token";
        Long assignmentId = 1L;
        AssignmentSubmission submission = new AssignmentSubmission();

        when(userService.hasRole(token, "Instructor")).thenReturn(true);
        when(assignmentService.getAssignmentSubmissionById(assignmentId)).thenReturn(Optional.of(submission));

        ResponseEntity<?> response = assignmentController.getAssignmentSubmissionById("Bearer " + token, assignmentId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(submission, response.getBody());
    }

    @Test
    void testGetAllSubmissions_ValidInstructorRole_ShouldReturnFilteredSubmissions() {
        String token = "valid-token";
        AssignmentSubmission submission1 = new AssignmentSubmission();
        submission1.setStudentId("1");
        AssignmentSubmission submission2 = new AssignmentSubmission();
        submission2.setStudentId("2");
        List<AssignmentSubmission> submissions = Arrays.asList(submission1, submission2);

        when(userService.hasRole(token, "Instructor")).thenReturn(true);
        when(assignmentService.findAllSubmissions()).thenReturn(submissions);

        ResponseEntity<List<AssignmentSubmission>> response = assignmentController.getAllSubmissions("Bearer " + token, "1", null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("1", response.getBody().get(0).getStudentId());
    }

    @Test
    void testGradeAssignment_ValidInstructorRole_ShouldGradeSuccessfully() {
        String token = "valid-token";
        Long assignmentId = 1L;
        Long score = 90L;
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setStudentId("1");

        when(userService.hasRole(token, "Instructor")).thenReturn(true);
        when(assignmentService.getAssignmentSubmissionById(assignmentId)).thenReturn(Optional.of(submission));
        when(userService.getUserById("1")).thenReturn(new user("1", "student123", "password123", "Student", "student@example.com"));
        

        ResponseEntity<?> response = assignmentController.gradeAssignment("Bearer " + token, assignmentId, score);

        assertEquals(200, response.getStatusCodeValue());
        verify(assignmentService, times(1)).gradeAssignment(assignmentId, score);
        verify(notificationService, times(1)).sendNotification(eq("1"), eq("Student"), anyString(), anyString(), eq(true));
    }

    @Test
    void testCreateAssignment_ValidInstructorRole_ShouldCreateSuccessfully() throws Exception {
        String token = "valid-token";
        String assignmentData = "{\"title\":\"Test Assignment\"}";
        MultipartFile file = mock(MultipartFile.class);
        Assignment assignment = new Assignment();

        when(userService.hasRole(token, "Instructor")).thenReturn(true);
        when(userService.getUserFromToken(token)).thenReturn(new user("1", "student123", "password123", "Student", "student@example.com"));

        when(objectMapper.readValue(assignmentData, Assignment.class)).thenReturn(assignment);

        ResponseEntity<String> response = assignmentController.createAssignment("Bearer " + token, assignmentData, file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Assignment created successfully.", response.getBody());
        verify(notificationService, times(1)).sendNotification(anyString(), eq("Instructor"), anyString(), anyString(), eq(true));
    }
}
