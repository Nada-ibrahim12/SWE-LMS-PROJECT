package com.example.demo.services;
import com.example.demo.model.Attendance;
import com.example.demo.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

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
    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }
    public void deleteById(Long id) {
        attendanceRepository.deleteById(id);
    }
}
