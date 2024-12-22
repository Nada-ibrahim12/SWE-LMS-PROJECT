package com.example.demo.model;

public class PerformanceRecord {

    private String Type; //"Quiz", "Assignment", "Attendance"
    private String description;
    private double scoreOrStatus; // Score for quizzes/assignments, Status for attendance.

    public PerformanceRecord() {
    }

    public PerformanceRecord(String Type, String description, double scoreOrStatus, String date) {
        this.Type = Type;
        this.description = description;
        this.scoreOrStatus = scoreOrStatus;

    }

    public String getType() {
        return this.Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public PerformanceRecord Type(String Type) {
        setType(Type);
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PerformanceRecord description(String description) {
        setDescription(description);
        return this;
    }

    public double getScoreOrStatus() {
        return this.scoreOrStatus;
    }

    public void setScoreOrStatus(double scoreOrStatus) {
        this.scoreOrStatus = scoreOrStatus;
    }

    public PerformanceRecord scoreOrStatus(double scoreOrStatus) {
        setScoreOrStatus(scoreOrStatus);
        return this;
    }

    // public String getDate() {
    //     return this.date;
    // }
    // public void setDate(String date) {
    //     this.date = date;
    // }
    // public PerformanceRecord date(String date) {
    //     setDate(date);
    //     return this;
    // }
    @Override
    public String toString() {
        return "{"
                + " Type='" + getType() + "'"
                + ", description='" + getDescription() + "'"
                + ", scoreOrStatus='" + getScoreOrStatus() + "'"
                + "}";
    }

}