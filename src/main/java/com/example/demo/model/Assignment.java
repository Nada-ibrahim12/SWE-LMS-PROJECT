package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String status;
    private String filePath;
    private String studentId;


//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private user student;\

    public Assignment(Long id, String title, String filePath, String studentId) {
        this.id = id;
        this.title = title;
        this.status = "Pending";
        this.filePath = filePath;
        this.studentId = studentId;
    }

    public Assignment() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

}
