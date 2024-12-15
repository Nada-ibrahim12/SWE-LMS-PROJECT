package com.example.demo.repository;

import com.example.demo.model.Attendance;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AttendanceRepository {

    public List<Attendance> attendanceList = new ArrayList<>();

    public List<Attendance> findAll() {
        return attendanceList;
    }
    public Optional<Attendance> findById(Long id) {
        return attendanceList.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }
    public Attendance save(Attendance attendance) {
//        setStudentAttendance(attendance.isAttend());
        attendanceList.add(attendance);
        return attendance;
    }
    public void deleteById(Long id) {
        attendanceList.removeIf(a -> a.getId().equals(id));
    }

    public Optional<Attendance> findByAttend(Boolean attend){
        return attendanceList.stream()
                .filter(a -> a.isAttend())
                .findAny();
    }
    public Optional<Attendance> findAttendanceOfStudent(Long id) {
        return attendanceList.stream()
                .filter(a -> a.getStudentId().equals(id))
                .findAny();
    }
    public Optional<Attendance> findAllAttendanceOfLesson(Long id) {
        return attendanceList.stream()
                .filter(a -> a.getLessonId().equals(id))
                .findAny();
    }
}
