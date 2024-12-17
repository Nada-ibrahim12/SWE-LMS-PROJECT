package com.example.demo.services;

import com.example.demo.model.Assignment;
import com.example.demo.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Assignment submitAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public void gradeAssignment(Long id, String feedback) {
        Assignment assignment = getAssignmentById(id);
        assignment.setStatus("Graded");
        assignmentRepository.save(assignment);
    }
}

