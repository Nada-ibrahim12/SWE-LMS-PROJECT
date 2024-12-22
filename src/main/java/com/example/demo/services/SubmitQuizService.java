package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Quiz;
import com.example.demo.model.QuizSubmission;
import com.example.demo.repository.QuizRepository;

@Service
public class SubmitQuizService {
    
    @Autowired
    private QuizRepository quizRepository;

    public QuizSubmission submitQuiz(QuizSubmission quizSubmission) {
        // Check if the quiz exists before submission
        Quiz quiz = quizRepository.findById(quizSubmission.getQuizId());
        if (quiz == null) {
            throw new RuntimeException("Quiz with ID " + quizSubmission.getQuizId() + " not found.");
        }

        // Optional: Validate the quiz submission (e.g., checking if the answers are not empty)
        if (quizSubmission.getAnswers() == null ) {
            throw new RuntimeException("Quiz answers cannot be empty.");
        }

        // Save and return the quiz submission
        return quizRepository.saveSubmissions(quizSubmission);
    }
    // public void gradeAssignment(Long submissionId, int grade, String feedback) {
    // // Find the submission by its ID
    // Submission submission = submissionRepository.findById(submissionId)
    //         .orElseThrow(() -> new RuntimeException("Submission not found"));

    // // Check if the submission status is "submitted"
    // if (!"submitted".equals(submission.getStatus())) {
    //     throw new IllegalStateException("Assignment must be submitted before grading");
    // }

    // // Set the grade and feedback
    // submission.setGrade(grade);
    // submission.setFeedback(feedback);

    // // Change the status to "graded"
    // submission.setStatus("graded");

    // // Save the updated submission in the repository
    // submissionRepository.save(submission);
}


