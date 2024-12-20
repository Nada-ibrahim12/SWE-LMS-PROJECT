package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.PerformanceRecord;
import com.example.demo.model.QuizSubmission;
import com.example.demo.repository.AssignmentRepository;
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

    // public List<PerformanceRecord> trackAssignmentSubmissions(String studentId) {
    //     List<PerformanceRecord> records = new ArrayList<>();
    //     List<Assignment> submissions = assignmentRepository.findAll()
    //            .stream().filter(A -> A.getStudentId().equals(studentId)).toList();
    // }
    // public List<PerformanceRecord> trackAttendance(String studentId) {
    //     // Fetch and return attendance records for the given student.
    // }
    // public List<PerformanceRecord> getProgressReport(String studentId) {
    //     // Aggregate quiz scores, assignment submissions, and attendance into a comprehensive report.
    // }
}
