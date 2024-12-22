package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Assignment;
import com.example.demo.model.Attendance;
import com.example.demo.model.AttendanceRecord;
import com.example.demo.model.PerformanceRecord;
import com.example.demo.model.QuizSubmission;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.QuizRepository;
import com.example.demo.repository.StudentRepository;

@Service
public class PerformanceTrackingService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<PerformanceRecord> trackQuizScoresByStudentId(String studentId) {

        List<PerformanceRecord> records = new ArrayList<>();
        List<QuizSubmission> submissions = quizRepository.findAllSubmissions()
                .stream().filter(Q -> Q.getStudentId().equals(studentId)).toList();

        for (QuizSubmission quizSubmission : submissions) {
            PerformanceRecord record = new PerformanceRecord();
            String description = quizRepository.findById(quizSubmission.getQuizId()).getDescription();
            double score = (double) quizSubmission.getScore();
            record.setType("Quiz");
            record.setDescription(description);
            record.setScoreOrStatus(score);
            records.add(record);

        }
        return records;

    }

    public List<PerformanceRecord> trackAssignmentSubmissions(String studentId) {
        List<PerformanceRecord> records = new ArrayList<>();
        List<Assignment> submissions = assignmentRepository.findAll()
                .stream().filter(A -> A.getStudentId().equals(studentId)).toList();

        for (Assignment assignment : submissions) {
            PerformanceRecord record = new PerformanceRecord();
            String description = assignment.getTitle();
            double score = (double) assignment.getScore();
            record.setType("Assignment");
            record.setDescription(description);
            record.setScoreOrStatus(score);
            records.add(record);
        }
        return records;
    }
    public List<PerformanceRecord> trackAttendance(String studentId) {
        List<PerformanceRecord> records = new ArrayList<>();
      
        List<Attendance> attendanceRecords = attendanceRepository.findAll()
                .stream()
                .filter(attendance -> attendance.getStudentId().toString().equals(studentId)) 
                .toList();
    
        for (Attendance attendance : attendanceRecords) {
            PerformanceRecord record = new PerformanceRecord();
            Long lessonId = attendance.getLessonId();
            Long courseId = attendance.getCourseId();
            String sessionDescription = "Lesson " + lessonId + " - Course " + courseId; 

            double status = attendance.isAttend(false) ? 1.0 : 0.0;
    
            record.setType("Attendance");
            record.setDescription(sessionDescription); 
            record.setScoreOrStatus(status); 
            
            String attendanceStatus = attendance.isAttend(false) ? "Attended" : "Not Attended";
            System.out.println("Attendance Status for session '" + sessionDescription + "': " + attendanceStatus);

            records.add(record);
        }
        
        return records;
    }
    
    
    public List<PerformanceRecord> getProgressReport(String studentId) {
        List<PerformanceRecord> progressReport = new ArrayList<>();

        progressReport.addAll(trackQuizScoresByStudentId(studentId));
        progressReport.addAll(trackAssignmentSubmissions(studentId));
        progressReport.addAll(trackAttendance(studentId));

        return progressReport;
    }
}
