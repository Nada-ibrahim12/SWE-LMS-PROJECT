package com.example.demo.services;

import com.example.demo.model.Assignment;
import com.example.demo.model.Course;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Optional;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private CourseRepository courseRepository;

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
        assignmentRepository.saveSubmissions(assignment);
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public void gradeAssignment(Long id, String feedback) {
        Optional<Assignment> assignment = getAssignmentById(id);
        assignment.get().setStatus("Graded");
        assignmentRepository.save(assignment.orElse(null));
    }
    public void createAssignment(Long courseId,Assignment assignment) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        assignment.setCourse(course);
        assignmentRepository.save(assignment);
    }
}

