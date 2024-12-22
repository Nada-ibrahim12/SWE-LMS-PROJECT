package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AssignmentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String filePath;
    private String studentId;
    private double score;

    public AssignmentSubmission(Long id, String filePath, String studentId, double score) {
        this.id = id;
        this.status = "Pending";
        this.filePath = filePath;
        this.studentId = studentId;
        this.score = score;
    }

    public AssignmentSubmission() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getUploadedFilePath() {
        return filePath;
    }
    public void setUploadedFilePath(String uploadedFilePath) {
        this.filePath = uploadedFilePath;
    }
    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }

}
