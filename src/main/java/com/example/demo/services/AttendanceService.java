package com.example.demo.services;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AttendanceRequest;
import com.example.demo.model.Attendance;
import com.example.demo.repository.AttendanceRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private OTPService otpService;

    public boolean markAttendance(AttendanceRequest request) {
        boolean isOtpValid = otpService.validateOtp(request.getCourseId(), request.getLessonId(), request.getOtp());

        if (!isOtpValid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(request.getStudentId());
        attendance.setCourseId(request.getCourseId());
        attendance.setLessonId(request.getLessonId());
        attendance.isAttend(false);
        attendance.setTimestamp(LocalDateTime.now());

        attendanceRepository.save(attendance);

        return true;
    }


    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }
    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }
    public Optional<Attendance> findByStatus(Boolean status) {
        return attendanceRepository.findByAttend(status);
    }
    public Optional<Attendance> findAttendanceOfStudent(Long id) {
        return attendanceRepository.findAttendanceOfStudent(id);
    }
    public Optional<Attendance> findAllAttendanceOfLesson(Long id) {
        return attendanceRepository.findAllAttendanceOfLesson(id);
    }
//    public Attendance save(Attendance attendance) {
//        return attendanceRepository.save(attendance);
//    }
//    public void deleteById(Long id) {
//        attendanceRepository.deleteById(id);
//    }
public String generateOTP() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder otp = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i <= 5; i++) {
        int index = random.nextInt(characters.length());
        otp.append(characters.charAt(index));
    }
    return otp.toString();
}
    public String generateAndStoreOtp(Long courseid, Long lessonid) {
        String otp = generateOTP();
        otpService.generateOtp(courseid, lessonid, otp);
        return otp;
    }
    public Optional<Attendance> getAttendanceForStudentInLesson(Long studentId, Long lessonId) {
        return attendanceRepository.findAttendanceByStudentAndLesson(studentId, lessonId);
    }
    
}
