package org.example.learning_managment_system;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.demo.dto.AttendanceRequest;
import com.example.demo.model.Attendance;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.services.AttendanceService;
import com.example.demo.services.OTPService;

class AttendanceControllerTest {

    @InjectMocks
    private AttendanceService attendanceService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private OTPService otpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateAndStoreOtp() {
        Long courseId = 1L;
        Long lessonId = 1L;
        String generatedOtp = "ABC123";
        doNothing().when(otpService).generateOtp(courseId, lessonId, generatedOtp);
        String otp = attendanceService.generateAndStoreOtp(courseId, lessonId);

        
        assertNotNull(otp);
        verify(otpService, times(1)).generateOtp(eq(courseId), eq(lessonId), anyString());
    }

    @Test
    void testMarkAttendance() {
        AttendanceRequest request = new AttendanceRequest();
        request.setStudentId(1L);
        request.setCourseId(1L);
        request.setLessonId(1L);
        request.setOtp("ABC123");

        when(otpService.validateOtp(1L, 1L, "ABC123")).thenReturn(true);

        Attendance attendance = new Attendance();
        attendance.setStudentId(1L);
        attendance.setCourseId(1L);
        attendance.setLessonId(1L);
        attendance.isAttend(false);

        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        boolean result = attendanceService.markAttendance(request);

        assertTrue(result);
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void testFindAllAttendance() {
        Attendance attendance1 = new Attendance();
        attendance1.setStudentId(1L);
        Attendance attendance2 = new Attendance();
        attendance2.setStudentId(2L);

        List<Attendance> mockAttendances = Arrays.asList(attendance1, attendance2);
        when(attendanceRepository.findAll()).thenReturn(mockAttendances);

        List<Attendance> attendances = attendanceService.findAll();

        assertEquals(2, attendances.size());
        verify(attendanceRepository, times(1)).findAll();
    }

    @Test
    void testFindAttendanceOfStudent() {
        Long studentId = 1L;
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);

        when(attendanceRepository.findAttendanceOfStudent(studentId)).thenReturn(Optional.of(attendance));

        Optional<Attendance> result = attendanceService.findAttendanceOfStudent(studentId);

        assertTrue(result.isPresent());
        assertEquals(studentId, result.get().getStudentId());
        verify(attendanceRepository, times(1)).findAttendanceOfStudent(studentId);
    }
}
