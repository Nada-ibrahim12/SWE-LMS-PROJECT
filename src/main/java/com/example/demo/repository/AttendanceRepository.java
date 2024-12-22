package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Attendance;

@Repository
public class AttendanceRepository {
    
    public List<Attendance> attendanceList = new ArrayList<>();

    public List<Attendance> findAll() {
        return attendanceList;
    }
    public Optional<Attendance> findById(Long id) {
        Optional<Attendance> attendance = attendanceList.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();

        if (attendance.isEmpty()) {
            System.out.println("Attendance not found");
        }
        return attendance;
    }

    public Attendance save(Attendance attendance) {
        attendanceList.add(attendance);
        return attendance;
    }

    public Optional<Attendance> findByAttend(Boolean attend) {
        Optional<Attendance> attendance =  attendanceList.stream()
                .filter(a -> a.isAttend(attend))
                .findAny();
        if (attendance.isEmpty()) {
            System.out.println("Attendance not found");
        }
        return attendance;
    }

    public Optional<Attendance> findAttendanceOfStudent(Long id) {
        Optional<Attendance> attendance =  attendanceList.stream()
                .filter(a -> a.getStudentId().equals(id))
                .findAny();
        if (attendance.isEmpty()) {
            System.out.println("Attendance not found");
        }
        return attendance;
    }
    public Optional<Attendance> findAllAttendanceOfLesson(Long id) {
        Optional<Attendance> attendance =  attendanceList.stream()
                .filter(a -> a.getLessonId().equals(id))
                .findAny();
        if (attendance.isEmpty()) {
            System.out.println("Attendance not found");
        }
      
        return attendance;
    }
    public Optional<Attendance> findAttendanceByStudentAndLesson(Long studentId, Long lessonId) {
        return attendanceList.stream()
                .filter(a -> a.getStudentId().equals(studentId) && a.getLessonId().equals(lessonId))
                .findFirst();
    }
    
}
