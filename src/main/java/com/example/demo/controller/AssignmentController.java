package com.example.demo.controller;

import com.example.demo.model.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.AssignmentService;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<Assignment> submitAssignment(@RequestBody Assignment assignment) {
        return ResponseEntity.ok(assignmentService.submitAssignment(assignment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<Void> gradeAssignment(@PathVariable Long id, @RequestBody String feedback) {
        assignmentService.gradeAssignment(id, feedback);
        return ResponseEntity.noContent().build();
    }
}

