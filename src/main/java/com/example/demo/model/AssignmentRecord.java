package com.example.demo.model;

import java.util.Objects;

public class AssignmentRecord extends PerformanceRecord{

    private String status;

    public AssignmentRecord(String description) {
        super("Assignment",description,0);
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
        if (Objects.equals(status, "Graded")) {
            setScoreOrStatus(1);
        } else {
            setScoreOrStatus(0);
        }
    }
    @Override
    public String toString() {
        return "{"
                + " Type='" + getType() + "'"
                + ", description='" + getDescription() + "'"
                + ", Status='" + getStatus() + "'"
                + "}";
    }













}
