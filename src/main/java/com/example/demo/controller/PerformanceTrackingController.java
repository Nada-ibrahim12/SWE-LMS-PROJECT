//package com.example.demo.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.model.PerformanceRecord;
//import com.example.demo.services.PerformanceTrackingService;
//
//@RestController
//@RequestMapping("/api/performance")
//public class PerformanceTrackingController {
//
//    @Autowired
//    private PerformanceTrackingService performanceTrackingService;
//
//    // @GetMapping("/quiz/{studentId}")
//    // public ResponseEntity<List<PerformanceRecord>> getQuizScores(@PathVariable String studentId) {
//    //     List<PerformanceRecord> records = performanceTrackingService.trackQuizScoresByStudentId(studentId);
//    //     return ResponseEntity.ok(records);
//    // }
//
//    // // Uncomment and implement these endpoints when the corresponding service methods are ready.
//
//    // @GetMapping("/assignment/{studentId}")
//    // public ResponseEntity<List<PerformanceRecord>> getAssignmentScores(@PathVariable String studentId) {
//    //     List<PerformanceRecord> records = performanceTrackingService.trackAssignmentSubmissions(studentId);
//    //     return ResponseEntity.ok(records);
//    // }
//
//    // @GetMapping("/attendance/{studentId}")
//    // public ResponseEntity<List<PerformanceRecord>> getAttendance(@PathVariable String studentId) {
//    //     List<PerformanceRecord> records = performanceTrackingService.trackAttendance(studentId);
//    //     return ResponseEntity.ok(records);
//    // }
//
//    // @GetMapping("/report/{studentId}")
//    // public ResponseEntity<List<PerformanceRecord>> getProgressReport(@PathVariable String studentId) {
//    //     List<PerformanceRecord> records = performanceTrackingService.getProgressReport(studentId);
//    //     return ResponseEntity.ok(records);
//    // }
//}
