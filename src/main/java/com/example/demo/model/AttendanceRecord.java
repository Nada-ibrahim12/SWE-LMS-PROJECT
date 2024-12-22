package com.example.demo.model;

import java.util.Objects;

public class AttendanceRecord {

    private String courseName;
    private Long courseID;
    private String lessonTitle;
    private String attendanceStatus;
    private String studentName;
    private String studentId;

    public AttendanceRecord() {
    }

    public AttendanceRecord(String courseName, Long courseID, String lessonTitle,
             String attendanceStatus, String studentName, String studentId) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.lessonTitle = lessonTitle;
        this.attendanceStatus = attendanceStatus;
        this.studentName = studentName;
        this.studentId = studentId;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public AttendanceRecord courseName(String courseName) {
        setCourseName(courseName);
        return this;
    }

    public Long getCourseID() {
        return this.courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    public AttendanceRecord courseID(Long courseID) {
        setCourseID(courseID);
        return this;
    }

    public String getLessonTitle() {
        return this.lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public AttendanceRecord lessonTitle(String lessonTitle) {
        setLessonTitle(lessonTitle);
        return this;
    }

    public String getAttendanceStatus() {
        return this.attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public AttendanceRecord attendanceStatus(String attendanceStatus) {
        setAttendanceStatus(attendanceStatus);
        return this;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public AttendanceRecord studentName(String studentName) {
        setStudentName(studentName);
        return this;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public AttendanceRecord studentId(String studentId) {
        setStudentId(studentId);
        return this;
    }

    @Override
    public String toString() {
        return "{"
                + " course Name='" + getCourseName() + "'"
                + ", lesson Title='" + getLessonTitle() + "'"
                + ", attendance Status='" + getAttendanceStatus() + "'"
                + ", student Name='" + getStudentName() + "'"
                + ", student Id='" + getStudentId() + "'"
                + "}";
    }

}
