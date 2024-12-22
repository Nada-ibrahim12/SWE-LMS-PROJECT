
package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Submission;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    
    // Find all submissions by assessment ID
    List<Submission> findByAssessmentId(Long assessmentId);

    // Find a submission by its assessment ID and status (e.g., to filter by pending or graded submissions)
    Optional<Submission> findByAssessmentIdAndStatus(Long assessmentId, String status);

    // Find a submission by student ID (assuming you have a student field in Submission)
    List<Submission> findByStudentId(Long studentId);

  
}
