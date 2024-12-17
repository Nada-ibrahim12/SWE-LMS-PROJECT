package com.example.demo.controller;

import com.example.demo.dto.AttendanceRequest;
import com.example.demo.model.Attendance;
import com.example.demo.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.AttendanceService;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(@RequestBody AttendanceRequest request) {
        try {
            attendanceService.markAttendance(request);
            return ResponseEntity.ok("Attendance marked successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestParam Long courseId, @RequestParam Long lessonId) {
        String otp = attendanceService.generateAndStoreOtp(courseId, lessonId);
        return ResponseEntity.ok("Generated OTP: " + otp);
    }
//    private OTPService otpService;
//
//    @GetMapping("/generateOtp")


    @GetMapping("/all")
    public List<Attendance> getAllAttendance() {
        return attendanceService.findAll();
    }

    @GetMapping("/id/{id}")
    public Optional<Attendance> getAttendanceById(@PathVariable Long id){
        return attendanceService.findById(id);
    }

    @GetMapping("/status/{status}")
    public Optional<Attendance> getByAttendanceStatus(@PathVariable Boolean status) {
        return attendanceService.findByStatus(status);
    }

//    @PostMapping("/add")
//    public Attendance addAttendance(@RequestBody Attendance attendance) {
//        return attendanceService.save(attendance);
//    }

//    @DeleteMapping("/{id}")
//    public void deleteAttendanceById(@PathVariable Long id) {
//        attendanceService.deleteById(id);
//    }

    @GetMapping("/student/{studentId}")
    public Optional<Attendance> findAttendanceOfStudent(@PathVariable Long studentId) {
        return attendanceService.findAttendanceOfStudent(studentId);
    }
    @GetMapping("/lesson/{lessonId}")
    public Optional<Attendance> findAllAttendanceOfLesson(@PathVariable Long lessonId) {
        return attendanceService.findAttendanceOfStudent(lessonId);
    }
}
