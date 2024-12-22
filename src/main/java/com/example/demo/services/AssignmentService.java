package com.example.demo.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.model.Assignment;
import com.example.demo.model.AssignmentSubmission;
import com.example.demo.repository.AssignmentRepository;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    // Assignment Submissions

    public void submitAssignmentWithFile(String studentId, AssignmentSubmission assignment, MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String uploadDir = "uploads/assignments submissions/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filePath = uploadPath.resolve(file.getOriginalFilename()).toString();
        Files.copy(file.getInputStream(), Paths.get(filePath));

        assignment.setUploadedFilePath(filePath);
        assignmentRepository.saveSubmission(assignment);
    }

    public Optional<AssignmentSubmission> getAssignmentSubmissionById(Long id) {
        return assignmentRepository.findSubmissionById(id);
    }

    public void deleteAssignmentSubmission(AssignmentSubmission assignment) {
        assignmentRepository.deleteSubmission(assignment);
    }

    public void deleteAssignmentSubmissionById(Long id) {
        assignmentRepository.deleteSubmissionById(id);
    }

    public List<AssignmentSubmission> findAllSubmissions() {
        return assignmentRepository.findAllSubmissions();
    }

    public void gradeAssignment(Long id, Long score) {
        Optional<AssignmentSubmission> assignmentOpt = getAssignmentSubmissionById(id);

        if (assignmentOpt.isPresent()) {
            AssignmentSubmission assignment = assignmentOpt.get();
            assignment.setStatus("Graded");
            assignment.setScore(score);
            assignmentRepository.saveSubmission(assignment);
        } else {
            throw new RuntimeException("Assignment not found with ID: " + id);
        }
    }

    // Assignment

    public void createAssignment(Assignment assignment, MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {

            String uploadDir = "uploads/assignments/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filePath = uploadPath.resolve(file.getOriginalFilename()).toString();
            Files.copy(file.getInputStream(), Paths.get(filePath));
            assignment.setAttachments(List.of(filePath));
        }

        assignment.setCreationDate(LocalDateTime.now());

        assignmentRepository.save(assignment);
    }

    public List<Assignment> searchAssignments(String keyword) {
        return assignmentRepository.searchByKeyword(keyword);
    }

    public Optional<Assignment> getAssignment(Long id) {
        return assignmentRepository.findById(id);
    }

    public List<Assignment> findAllAssignments() {
        return assignmentRepository.findAll();
    }

    public void deleteAssignment(Assignment assignment) {
        assignmentRepository.delete(assignment);
    }
    public void deleteAssignmentById(Long id) {
        assignmentRepository.deleteById(id);
    }
}

