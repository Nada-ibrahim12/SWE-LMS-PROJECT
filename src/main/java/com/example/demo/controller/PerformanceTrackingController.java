package com.example.demo.controller;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.AssignmentRecord;
import com.example.demo.model.PerformanceRecord;
import com.example.demo.model.ProgressReport;
import com.example.demo.services.PerformanceTrackingService;
import com.example.demo.services.UserService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class PerformanceTrackingController {

    @Autowired
    private PerformanceTrackingService performanceTrackingService;

    @Autowired
    private UserService userService;

    @GetMapping("/instructor/{instructorId}/course/{courseId}/student/{studentId}/quiz-scores")
    public ResponseEntity<List<PerformanceRecord>> getQuizScores(
            @RequestHeader("Authorization") String token,
            @PathVariable String instructorId,
            @PathVariable Long courseId,
            @PathVariable String studentId) {
        if (!userService.hasRole(token, "Instructor")) {
            return ResponseEntity.status(403).build();
        }
        List<PerformanceRecord> performanceRecords = performanceTrackingService
                .trackQuizScoresByStudentId(instructorId, courseId, studentId);
        return ResponseEntity.ok(performanceRecords);
    }

    @GetMapping("/instructor/{instructorId}/course/{courseId}/student/{studentId}/assignments")
    public ResponseEntity<List<AssignmentRecord>> getAssignments(
            @RequestHeader("Authorization") String token,
            @PathVariable String instructorId,
            @PathVariable Long courseId,
            @PathVariable String studentId) {
        if (!userService.hasRole(token, "Instructor")) {
            return ResponseEntity.status(403).build();
        }
        List<AssignmentRecord> assignmentRecords = performanceTrackingService
                .trackAssignmentsByStudentId(instructorId, courseId, studentId);
        return ResponseEntity.ok(assignmentRecords);
    }

    @GetMapping("/instructor/{instructorId}/course/{courseId}/getAllStudentsQuizzes")
    public ResponseEntity<List<PerformanceRecord>> getAllStudentsQuizzes(@RequestHeader("Authorization") String token, @PathVariable String instructorId, @PathVariable Long courseId) {
        if (!userService.hasRole(token, "Instructor")) {
            return ResponseEntity.status(403).build();
        }
        List<PerformanceRecord> performanceRecords = performanceTrackingService
                .trackQuizScoresForAllStudentsWithNames(instructorId, courseId);
        return ResponseEntity.status(HttpStatus.SC_OK).body(performanceRecords);

    }

    @GetMapping("/instructor/{instructorId}/course/{courseId}/getAllStudentsAssignments")
    public ResponseEntity<List<AssignmentRecord>> getAllStudentsAssignments(@RequestHeader("Authorization") String token, @PathVariable String instructorId, @PathVariable Long courseId) {
        if (!userService.hasRole(token, "Instructor")) {
            return ResponseEntity.status(403).build();
        }
        List<AssignmentRecord> performanceRecords = performanceTrackingService
                .trackAssignmentsForAllStudentsWithNames(instructorId, courseId);
        return ResponseEntity.status(HttpStatus.SC_OK).body(performanceRecords);

    }

    //Excel sheets download
    @GetMapping("/instructor/{instructorId}/course/{courseId}/downloadQuizScores")
    public ResponseEntity<byte[]> downloadQuizScores(
            @RequestHeader("Authorization") String token,
            @PathVariable String instructorId,
            @PathVariable Long courseId) throws IOException {

        // Check for valid instructor role
        if (!userService.hasRole(token, "Instructor")) {
            return ResponseEntity.status(403).build();
        }

        // Fetch quiz performance data
        List<PerformanceRecord> performanceRecords = performanceTrackingService
                .trackQuizScoresForAllStudentsWithNames(instructorId, courseId);

        // Create Excel file for quizzes
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Workbook workbook = new XSSFWorkbook();

            // Create quiz sheet
            Sheet quizSheet = workbook.createSheet("Quiz Scores");
            createQuizSheetHeader(quizSheet);
            populateQuizSheet(quizSheet, performanceRecords);

            // Write the workbook to ByteArrayOutputStream
            workbook.write(baos);
            workbook.close();

            // Set headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=quiz_scores.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
        }
    }

    @GetMapping("/instructor/{instructorId}/course/{courseId}/downloadAssignments")
    public ResponseEntity<byte[]> downloadAssignments(
            @RequestHeader("Authorization") String token,
            @PathVariable String instructorId,
            @PathVariable Long courseId) throws IOException {

        // Check for valid instructor role
        if (!userService.hasRole(token, "Instructor")) {
            return ResponseEntity.status(403).build();
        }

        // Fetch assignment performance data
        List<AssignmentRecord> assignmentRecords = performanceTrackingService
                .trackAssignmentsForAllStudentsWithNames(instructorId, courseId);

        // Create Excel file for assignments
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Workbook workbook = new XSSFWorkbook();

            // Create assignment sheet
            Sheet assignmentSheet = workbook.createSheet("Assignments");
            createAssignmentSheetHeader(assignmentSheet);
            populateAssignmentSheet(assignmentSheet, assignmentRecords);

            // Write the workbook to ByteArrayOutputStream
            workbook.write(baos);
            workbook.close();

            // Set headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=assignments.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
        }
    }

    // Helper methods to create headers and populate data for quizzes
    private void createQuizSheetHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Student Name");
        headerRow.createCell(1).setCellValue("Quiz Description");
        headerRow.createCell(2).setCellValue("Score");
    }

    private void populateQuizSheet(Sheet sheet, List<PerformanceRecord> records) {
        int rowNum = 1;
        for (PerformanceRecord record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getStudentName());
            row.createCell(1).setCellValue(record.getDescription());
            row.createCell(2).setCellValue(record.getScoreOrStatus());
        }
    }

    // Helper methods to create headers and populate data for assignments
    private void createAssignmentSheetHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Student Name");
        headerRow.createCell(1).setCellValue("Assignment Description");
        headerRow.createCell(2).setCellValue("Status");
    }

    private void populateAssignmentSheet(Sheet sheet, List<AssignmentRecord> records) {
        int rowNum = 1;
        for (AssignmentRecord record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getStudentName());
            row.createCell(1).setCellValue(record.getDescription());
            row.createCell(2).setCellValue(record.getStatus());
        }
    }

    //*********************************************
    @GetMapping("/instructor/{instructorId}/course/{courseId}/student/{studentId}/progressReport")
    public ResponseEntity<ProgressReport> getProgressReport(
            @RequestHeader("Authorization") String token,
            @PathVariable String instructorId,
            @PathVariable Long courseId,
            @PathVariable String studentId) {

        // Check for valid instructor role
        if (!userService.hasRole(token, "Instructor")) {
            return ResponseEntity.status(403).build();
        }

        ProgressReport report = performanceTrackingService.fetchProgressReport(instructorId, courseId, studentId);

        if (report != null) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{instructorId}/course/{courseId}/progressReports")
    public ResponseEntity<List<ProgressReport>> getProgressReportsForAllStudents(
            @RequestHeader("Authorization") String token,
            @PathVariable String instructorId,
            @PathVariable Long courseId) {

        // Validate that the user has instructor role
        if (!userService.hasRole(token, "Instructor")) {
            return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).build(); // 403 Forbidden
        }

        List<ProgressReport> progressReports = performanceTrackingService.fetchProgressReportsForAllStudents(instructorId, courseId);
        if (progressReports.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
        }

        return ResponseEntity.ok(progressReports);
    }

}
