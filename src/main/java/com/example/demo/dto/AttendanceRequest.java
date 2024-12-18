package com.example.demo.dto;

public class AttendanceRequest {
    private Long studentId;
    private Long courseId;
    private Long lessonId;
    private String otp;

    AttendanceRequest(){}
    public AttendanceRequest(Long studentId, Long courseId, Long lessonId, String otp) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.otp = otp;
    }
    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public Long getLessonId() {
        return lessonId;
    }
    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }
    public String getOtp() {
        return otp;
    }
    public void setOtp(String generateOTP) {
        this.otp = generateOTP;
    }

}
