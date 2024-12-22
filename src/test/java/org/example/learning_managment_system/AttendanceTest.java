package org.example.learning_managment_system;
import com.example.demo.model.Attendance;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

class AttendanceTest {

    private Attendance attendance;

    @BeforeEach
    void setUp() {
        attendance = new Attendance();
        attendance.setId(1L);
        attendance.setLessonId(101L);
        attendance.setStudentId(1L);
        attendance.setCourseId(201L);
        attendance.setIsAttend(true);
        attendance.setTimestamp(LocalDateTime.now());
    }

    @Test
    void testGetStatus() {
        assertEquals("Attended", attendance.getStatus());
    }

    @Test
    void testGenerateOTP() {
        String otp = attendance.generateOTP();
        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("[A-Z0-9]+"));
    }
}
