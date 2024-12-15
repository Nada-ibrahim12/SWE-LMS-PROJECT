package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lessonId;

    @Column(nullable = false)
    private Long studentId;

    @Column(length = 6, nullable = false)
    private String otp;

    @Column(nullable = false)
    private boolean isAttend;

//    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Attendance() {}

    public Attendance(Long id, Long lessonId, Long studentId, String otp, boolean isAttend, LocalDateTime timestamp) {
        this.id = id;
        this.lessonId = lessonId;
        this.studentId = studentId;
        this.otp = otp;
        this.isAttend = isAttend;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public boolean isAttend() {
        return isAttend;
    }

    public void SetIsAttend(boolean isAttend) {
        this.isAttend = isAttend;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
