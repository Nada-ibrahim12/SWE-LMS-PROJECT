package com.example.demo.controller;

import com.example.demo.model.Attendance;
import com.example.demo.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.AttendanceService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;
    private OTPService otpService;

    @GetMapping("/generateOtp")
    public String generateOtp() {
        return otpService.generateOTP();
    }
    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Attendance> getAttendanceById(@PathVariable Long id){
        return attendanceService.findById(id);
    }

    @GetMapping("/status/{status}")
    public Optional<Attendance> getByAttendanceStatus(@PathVariable Boolean status) {
        return attendanceService.findByStatus(status);
    }

    @PostMapping("/add")
    public Attendance addAttendance(@RequestBody Attendance attendance) {
        return attendanceService.save(attendance);
    }

    @DeleteMapping("/{id}")
    public void deleteAttendanceById(@PathVariable Long id) {
        attendanceService.deleteById(id);
    }

    @GetMapping("/{studentid}")
    public Optional<Attendance> findAttendanceOfStudent(@PathVariable Long studentId) {
        return attendanceService.findAttendanceOfStudent(studentId);
    }
    @GetMapping("/{lessonid}")
    public Optional<Attendance> findAllAttendanceOfLesson(@PathVariable Long lessonId) {
        return attendanceService.findAttendanceOfStudent(lessonId);
    }
}
