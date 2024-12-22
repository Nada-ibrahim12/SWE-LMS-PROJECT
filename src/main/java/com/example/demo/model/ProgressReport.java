package com.example.demo.model;

import java.util.List;
import java.util.Objects;

public class ProgressReport {

    private String studentName;
    private String courseName;
    private List<PerformanceRecord> quizRecords;
    private List<AssignmentRecord> assignmentRecords;

    public ProgressReport() {
    }

    public ProgressReport(String studentName, String courseName, List<PerformanceRecord> quizRecords, List<AssignmentRecord> assignmentRecords) {
        this.studentName = studentName;
        this.courseName = courseName;
        this.quizRecords = quizRecords;
        this.assignmentRecords = assignmentRecords;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public ProgressReport studentName(String studentName) {
        setStudentName(studentName);
        return this;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ProgressReport courseName(String courseName) {
        setCourseName(courseName);
        return this;
    }

    public List<PerformanceRecord> getQuizRecords() {
        return this.quizRecords;
    }

    public void setQuizRecords(List<PerformanceRecord> quizRecords) {
        this.quizRecords = quizRecords;
    }

    public ProgressReport quizRecords(List<PerformanceRecord> quizRecords) {
        setQuizRecords(quizRecords);
        return this;
    }

    public List<AssignmentRecord> getAssignmentRecords() {
        return this.assignmentRecords;
    }

    public void setAssignmentRecords(List<AssignmentRecord> assignmentRecords) {
        this.assignmentRecords = assignmentRecords;
    }

    public ProgressReport assignmentRecords(List<AssignmentRecord> assignmentRecords) {
        setAssignmentRecords(assignmentRecords);
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentName, courseName, quizRecords, assignmentRecords);
    }

    @Override
    public String toString() {
        return "{"
                + " studentName='" + getStudentName() + "'"
                + ", courseName='" + getCourseName() + "'"
                + ", quizRecords='" + getQuizRecords() + "'"
                + ", assignmentRecords='" + getAssignmentRecords() + "'"
                + "}";
    }

}
