package com.example.demo.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Assignment;
import com.example.demo.repository.AssignmentRepository;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public void submitAssignmentWithFile(Assignment assignment, MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String uploadDir = "uploads/assignments/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filePath = uploadPath.resolve(file.getOriginalFilename()).toString();
        Files.copy(file.getInputStream(), Paths.get(filePath));

        assignment.setUploadedFilePath(filePath);
        assignmentRepository.save(assignment);
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public void gradeAssignment(Long id, String feedback) {
        Optional<Assignment> assignment = getAssignmentById(id);
        assignment.get().setStatus("Graded");
        assignmentRepository.save(assignment.orElse(null));
    }

    public Assignment createAssignment(String title, Long courseId) {
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setCourseId(courseId);
        assignment.setStatus("Pending");
        return assignmentRepository.save(assignment);
    }
}

