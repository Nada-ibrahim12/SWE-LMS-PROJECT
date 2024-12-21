package com.example.demo.services;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Assignment;
import com.example.demo.model.AssignmentRecord;
import com.example.demo.model.Course;
import com.example.demo.model.PerformanceRecord;
import com.example.demo.model.Quiz;
import com.example.demo.model.QuizSubmission;
import com.example.demo.model.user;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.QuizRepository;
import com.example.demo.repository.StudentRepository;

import io.micrometer.core.instrument.MultiGauge.Row;

@Service
public class PerformanceTrackingService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private CourseRepository courseRepository;

    // Track quiz scores for a student in a specific course by instructor
    public List<PerformanceRecord> trackQuizScoresByStudentId(String instructorId, Long courseId, String studentId) {
        List<PerformanceRecord> records = new ArrayList<>();

        // Check if instructor manages the course
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent() && courseOptional.get().getInstructor().getUserId().equals(instructorId)) {
            // Check if student is enrolled in the course
            Course course = courseOptional.get();
            if (course.getEnrolledStudents().stream().anyMatch(student -> student.getUserId().equals(studentId))) {

                // Fetch quiz submissions for the student
                List<Long> quizIds = quizRepository.quizzes
                        .stream()
                        .filter(Q -> Q.getCourse().getId().equals(courseId))
                        .map(Quiz::getId)
                        .toList();

                // courses'quizes ids
                List<QuizSubmission> submissions = new ArrayList<>();
                for (Long quizId : quizIds) {
                    submissions.addAll(quizRepository.submissions.stream().filter(submission -> submission.getQuizId().equals(quizId)).toList());
                }

                submissions = quizRepository.findAllSubmissions()
                        .stream().filter(submission -> submission.getStudentId().equals(studentId))
                        .toList();
                for (QuizSubmission quizSubmission : submissions) {
                    PerformanceRecord record = new PerformanceRecord();
                    String description = quizRepository.findById(quizSubmission.getQuizId()).getDescription();
                    double score = quizSubmission.getScore();
                    record.setType("Quiz");
                    record.setDescription(description);
                    record.setScoreOrStatus(score);
                    records.add(record);
                }
            }
        }
        return records;
    }

    public List<AssignmentRecord> trackAssignmentsByStudentId(String instructorId, Long courseId, String studentId) {
        List<AssignmentRecord> records = new ArrayList<>();
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent() && courseOptional.get().getInstructor().getUserId().equals(instructorId)) {
            Course course = courseOptional.get();
            if (course.getEnrolledStudents().stream().anyMatch(student -> student.getUserId().equals(studentId))) {

                List<Assignment> assignments = assignmentRepository.findAll().stream()
                        .filter(A -> A.getStudentId().equals(studentId)).toList();
                for (Assignment assignment : assignments) {
                    AssignmentRecord record = new AssignmentRecord(assignment.getTitle());
                    record.setStatus(assignment.getStatus());
                    records.add(record);

                }
            }

        }
        return records;
    }

    public List<PerformanceRecord> trackQuizScoresForAllStudentsWithNames(String instructorId, Long courseId) {
        List<PerformanceRecord> records = new ArrayList<>();

        // Check if instructor manages the course
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent() && courseOptional.get().getInstructor().getUserId().equals(instructorId)) {
            Course course = courseOptional.get();

            // Fetch quizzes for the course
            List<Long> quizIds = quizRepository.quizzes
                    .stream()
                    .filter(Q -> Q.getCourse().getId().equals(courseId))
                    .map(Quiz::getId)
                    .toList();

            // Fetch quiz submissions for all enrolled students
            for (user student : course.getEnrolledStudents()) {
                String studentName = student.getUsername();
                String studentId = student.getUserId();

                List<QuizSubmission> submissions = quizRepository.submissions.stream()
                        .filter(submission -> submission.getStudentId().equals(studentId) && quizIds.contains(submission.getQuizId()))
                        .toList();

                for (QuizSubmission quizSubmission : submissions) {
                    PerformanceRecord record = new PerformanceRecord();
                    String description = quizRepository.findById(quizSubmission.getQuizId()).getDescription();
                    double score = quizSubmission.getScore();

                    record.setType("Quiz");
                    record.setDescription(description);
                    record.setScoreOrStatus(score);
                    record.setStudentName(studentName);
                    records.add(record);
                }
            }
        }
        return records;
    }

    public List<AssignmentRecord> trackAssignmentsForAllStudentsWithNames(String instructorId, Long courseId) {
        List<AssignmentRecord> records = new ArrayList<>();

        // Check if instructor manages the course
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent() && courseOptional.get().getInstructor().getUserId().equals(instructorId)) {
            Course course = courseOptional.get();

            // Fetch assignments for all enrolled students
            for (user student : course.getEnrolledStudents()) {
                String studentName = student.getUsername();
                String studentId = student.getUserId();

                List<Assignment> assignments = assignmentRepository.findAll().stream()
                        .filter(A -> A.getStudentId().equals(studentId))
                        .toList();

                for (Assignment assignment : assignments) {
                    AssignmentRecord record = new AssignmentRecord(assignment.getTitle());
                    record.setScoreOrStatus(assignment.getGrade());
                    record.setStudentName(studentName);
                    records.add(record);
                }
            }
        }
        return records;
    }

    public byte[] generateQuizScoresExcel(String instructorId, Long courseId) throws Exception {
        List<PerformanceRecord> quizRecords = trackQuizScoresForAllStudentsWithNames(instructorId, courseId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Quiz Scores");
            Row headerRow = sheet.createRow(0);

            // Headers
            headerRow.createCell(0).setCellValue("Student Name");
            headerRow.createCell(1).setCellValue("Quiz Description");
            headerRow.createCell(2).setCellValue("Score");

            // Populate rows
            int rowNum = 1;
            for (PerformanceRecord record : quizRecords) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getStudentName());
                row.createCell(1).setCellValue(record.getDescription());
                row.createCell(2).setCellValue(record.getScoreOrStatus());
            }

            // Convert to byte array
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    // Generate Excel for Assignment Records
    public byte[] generateAssignmentRecordsExcel(String instructorId, Long courseId) throws Exception {
        List<AssignmentRecord> assignmentRecords = trackAssignmentsForAllStudentsWithNames(instructorId, courseId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = (Sheet) workbook.createSheet("Assignment Records");
            Row headerRow = sheet.createRow(0);

            // Headers
            headerRow.createCell(0).setCellValue("Student Name");
            headerRow.createCell(1).setCellValue("Assignment Title");
            headerRow.createCell(2).setCellValue("Score/Status");

            // Populate rows
            int rowNum = 1;
            for (AssignmentRecord record : assignmentRecords) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getStudentName());
                row.createCell(1).setCellValue(record.getTitle());
                row.createCell(2).setCellValue(record.getScoreOrStatus());
            }

            // Convert to byte array
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }

    }
}

// Track assignment scores for a student in a specific course by instructor
// public List<PerformanceRecord> trackAssignmentScoresByInstructor(String instructorId, Long courseId, String studentId) {
//     List<PerformanceRecord> records = new ArrayList<>();
//     // Check if instructor manages the course
//     Optional<Course> courseOptional = courseRepository.findById(courseId);
//     if (courseOptional.isPresent() && courseOptional.get().getInstructor().getUserId().equals(instructorId)) {
//         // Check if student is enrolled in the course
//         Course course = courseOptional.get();
//         if (course.getEnrolledStudents().stream().anyMatch(student -> student.getUserId().equals(studentId))) {
//             // Fetch assignments for the student
//             List<Assignment> assignments = assignmentRepository.findAll()
//                     .stream().filter(assignment -> assignment.getStudentId().equals(studentId) && assignment.getCourseId().equals(courseId))
//                     .toList();
//             for (Assignment assignment : assignments) {
//                 PerformanceRecord record = new PerformanceRecord();
//                 record.setType("Assignment");
//                 record.setDescription(assignment.getTitle());
//                 record.setScoreOrStatus(assignment.getGrade());
//                 records.add(record);
//             }
//         }
//     }
//     return records;
// }
// // Track attendance for a student in a specific course by instructor
// public List<PerformanceRecord> trackAttendanceByInstructor(String instructorId, Long courseId, String studentId) {
//     List<PerformanceRecord> records = new ArrayList<>();
//     // Check if instructor manages the course
//     Optional<Course> courseOptional = courseRepository.findById(courseId);
//     if (courseOptional.isPresent() && courseOptional.get().getInstructor().getUserId().equals(instructorId)) {
//         // Check if student is enrolled in the course
//         Course course = courseOptional.get();
//         if (course.getEnrolledStudents().stream().anyMatch(student -> student.getUserId().equals(studentId))) {
//             // Fetch attendance record for the student
//             Student student = (Student) studentRepository.findById(studentId).orElse(null);
//             if (student != null && student.getAttendanceList().containsKey(courseId)) {
//                 Boolean attended = student.getAttendanceList().get(courseId);
//                 PerformanceRecord record = new PerformanceRecord();
//                 record.setType("Attendance");
//                 record.setDescription("Attendance for course: " + course.getTitle());
//                 record.setScoreOrStatus(attended ? "Present" : "Absent");
//                 records.add(record);
//             }
//         }
//     }
//     return records;
// }
// // Aggregates quiz scores, assignments, and attendance for a comprehensive report
// public List<PerformanceRecord> getComprehensiveProgressReport(String instructorId, Long courseId, String studentId) {
//     List<PerformanceRecord> report = new ArrayList<>();
//     // Track Quiz Scores
//     report.addAll(trackQuizScoresByInstructor(instructorId, courseId, studentId));
//     // Track Assignment Scores
//     report.addAll(trackAssignmentScoresByInstructor(instructorId, courseId, studentId));
//     // Track Attendance
//     report.addAll(trackAttendanceByInstructor(instructorId, courseId, studentId));
//     return report;
// }
/**
 * ******************************************************************************************
 *******************
 * *************************************************************************
 */
// package com.example.demo.services;
// import java.util.ArrayList;
// import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import com.example.demo.model.Assignment;
// import com.example.demo.model.PerformanceRecord;
// import com.example.demo.model.QuizSubmission;
// import com.example.demo.repository.AssignmentRepository;
// import com.example.demo.repository.QuizRepository;
// import com.example.demo.repository.StudentRepository;
// @Service
// public class PerformanceTrackingService {
//     @Autowired
//     private StudentRepository studentRepository;
//     @Autowired
//     private AssignmentRepository assignmentRepository;
//     @Autowired
//     private QuizRepository quizRepository;
//     public List<PerformanceRecord> trackQuizScoresByStudentId(String studentId) {
//         List<PerformanceRecord> records = new ArrayList<>();
//         List<QuizSubmission> submissions = quizRepository.findAllSubmissions()
//                 .stream().filter(Q -> Q.getStudentId().equals(studentId)).toList();
//         for (QuizSubmission quizSubmission : submissions) {
//             PerformanceRecord record = new PerformanceRecord();
//             String description = quizRepository.findById(quizSubmission.getQuizId()).getDescription();
//             double score = (double) quizSubmission.getScore();
//             record.setType("Quiz");
//             record.setDescription(description);
//             record.setScoreOrStatus(score);
//             records.add(record);
//         }
//         return records;
//     }
//     public List<PerformanceRecord> trackAssignmentScoresByStudentId(String studentId) {
//         List<PerformanceRecord> records = new ArrayList<>();
//         List<Assignment> assignments = assignmentRepository.findAll().stream()
//                 .filter(A -> A.getStudentId().equals(studentId)).toList();
//         for (Assignment assignment : assignments) {
//             PerformanceRecord record = new PerformanceRecord();
//             record.setType("Assignment");
//             record.setDescription(assignment.getTitle());
//             record.setScoreOrStatus(assignment.getGrade());
//             records.add(record);
//         }
//         return records;
//     }
//     // public List<PerformanceRecord> trackAssignmentSubmissions(String studentId) {
//     //     List<PerformanceRecord> records = new ArrayList<>();
//     //     List<Assignment> submissions = assignmentRepository.findAll()
//     //            .stream().filter(A -> A.getStudentId().equals(studentId)).toList();
//     // }
//     // public List<PerformanceRecord> trackAttendance(String studentId) {
//     //     // Fetch and return attendance records for the given student.
//     // }
//     // public List<PerformanceRecord> getProgressReport(String studentId) {
//     //     // Aggregate quiz scores, assignment submissions, and attendance into a comprehensive report.
//     // }
// }
