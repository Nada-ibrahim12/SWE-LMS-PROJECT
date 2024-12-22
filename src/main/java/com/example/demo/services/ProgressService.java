// package com.example.demo.services;

// import com.example.demo.model.Attendance;
// import com.example.demo.model.ProgressReport;
// import com.example.demo.model.Quiz;
// import com.example.demo.model.Assignment;
// import com.example.demo.repository.AttendanceRepository;
// import com.example.demo.repository.ProgressRepository;
// import com.example.demo.repository.QuizRepository;
// import com.example.demo.repository.AssignmentRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class ProgressService {

//     @Autowired
//     private ProgressReportRepository progressReportRepository;

//     @Autowired
//     private QuizRepository quizRepository;

//     @Autowired
//     private AttendanceRepository attendanceRepository;

//     @Autowired
//     private AssignmentRepository assignmentRepository;

//     public List<ProgressReport> getProgressReportsByCourseId(Long courseId) {
//         return progressReportRepository.findByCourseId(courseId);
//     }

//     public ProgressReport getProgressReportByStudentId(String studentId) {
//         return progressReportRepository.findByStudentId(studentId)
//                 .orElseThrow(() -> new RuntimeException("Progress report not found for student: " + studentId));
//     }

//     public List<Attendance> getAttendanceByCourseId(Long courseId) {
//         return attendanceRepository.findByCourseId(courseId);
//     }

//     public List<Quiz> getQuizzesByCourseId(Long courseId) {
//         return quizRepository.findByCourseId(courseId);
//     }

//     public List<Assignment> getAssignmentsByStudentId(String studentId) {
//         return assignmentRepository.findByStudentId(studentId);
//     }

//     public void saveProgressReport(ProgressReport report) {
//         progressReportRepository.save(report);
//     }
// }