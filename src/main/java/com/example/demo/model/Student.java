package com.example.demo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends user {

    public Student(String userId, String username, String password, String role, String email) {
        super(userId, username, password, role, email);
    }

    private String studentEmail;
    private List<String> enrolledCourses = new ArrayList<String>();
    private Map<Long, Boolean> AttendanceList = new HashMap<Long, Boolean>();

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = getEmail();
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public Map<Long, Boolean> getAttendanceList() {
        return AttendanceList;
    }

    public void setAttendanceList(Map<Long, Boolean> attendanceList) {
        AttendanceList = attendanceList;
    }

    public void addEnrolledCourse(String courseTitle) {
        if (!enrolledCourses.contains(courseTitle)) {
            enrolledCourses.add(courseTitle);
        }
    }
}
