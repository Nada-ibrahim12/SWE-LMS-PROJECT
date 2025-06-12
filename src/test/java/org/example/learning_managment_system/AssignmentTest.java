package org.example.learning_managment_system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.model.AssignmentSubmission;

public class AssignmentTest {

    private AssignmentSubmission assignment;

    @BeforeEach
    void setUp() {
        
        assignment = new AssignmentSubmission(1L, "/path/to/file", "student123", 85.0);
    }

    @Test
    void testConstructor() {
        assertNotNull(assignment);
        assertEquals(1L, assignment.getId());
        assertEquals("/path/to/file", assignment.getUploadedFilePath());
        assertEquals("student123", assignment.getStudentId());
        assertEquals(85.0, assignment.getScore());
        assertEquals("Pending", assignment.getStatus());
    }

    @Test
    void testSetId() {
        assignment.setId(2L);
        assertEquals(2L, assignment.getId());
    }

    @Test
    void testSetStatus() {
        assignment.setStatus("Submitted");
        assertEquals("Submitted", assignment.getStatus());
    }

    @Test
    void testSetFilePath() {
        assignment.setUploadedFilePath("/new/path/to/file");
        assertEquals("/new/path/to/file", assignment.getUploadedFilePath());
    }

    @Test
    void testSetStudentId() {
        assignment.setStudentId("student456");
        assertEquals("student456", assignment.getStudentId());
    }

    @Test
    void testSetScore() {
        assignment.setScore(90.0);
        assertEquals(90.0, assignment.getScore());
    }
}
