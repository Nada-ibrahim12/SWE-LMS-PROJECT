package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.demo.model.AssignmentSubmission;
import com.example.demo.model.Assignment;
@Repository
public class AssignmentRepository {

    public List<AssignmentSubmission> assignmentSubmissions = new ArrayList<>();
    public List<Assignment> assignments = new ArrayList<>();



    // Assignment Submissions

    public List<AssignmentSubmission> findAllSubmissions() {
        return assignmentSubmissions;
    }

    public Optional<AssignmentSubmission> findSubmissionById(Long id) {
        return assignmentSubmissions.stream()
                .filter(assignment -> assignment.getId().equals(id))
                .findFirst();
    }

    public AssignmentSubmission saveSubmission(AssignmentSubmission assignment) {
        assignmentSubmissions.add(assignment);
        return assignment;
    }


    public void deleteSubmission(AssignmentSubmission assignment) {
        assignmentSubmissions.remove(assignment);
    }

    public void deleteSubmissionById(Long id) {
        Optional<AssignmentSubmission> assignment = findSubmissionById(id);
        assignment.ifPresent(assignmentSubmissions::remove);
    }


    // Assignment

    public Assignment save(Assignment assignment) {
        assignments.add(assignment);
        return assignment;
    }
    public Optional<Assignment> findById(Long id) {
        return assignments.stream()
                .filter(assignment -> assignment.getId().equals(id))
                .findFirst();
    }

    public List<Assignment> findAll() {
        return assignments;
    }
    public Assignment update(Assignment assignment) {
        assignments.remove(assignment);
        assignments.add(assignment);
        return assignment;
    }
    public void delete(Assignment assignment) {
        assignments.remove(assignment);
    }

    public void deleteById(Long id) {
        Optional<Assignment> assignment = findById(id);
        assignment.ifPresent(assignments::remove);
    }

    public List<Assignment> findByCourseId(Long courseId) {
        return assignments.stream()
                .filter(a -> courseId != null && courseId.equals(a.getCourseId()))
                .collect(Collectors.toList());
    }

    public List<Assignment> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>();
        }

        return assignments.stream()
                .filter(a -> (a.getTitle() != null && a.getTitle().toLowerCase().contains(keyword.toLowerCase())) ||
                        (a.getDescription() != null && a.getDescription().toLowerCase().contains(keyword.toLowerCase())) ||
                        (a.getInstructorId() != null && a.getInstructorId().toLowerCase().contains(keyword.toLowerCase())))
                        .collect(Collectors.toList());
    }
}
