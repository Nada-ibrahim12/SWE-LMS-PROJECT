package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Quiz;
import com.example.demo.model.QuizSubmission;

@Repository
public class QuizRepository {

    // Sample in-memory data storage for quizzes and quiz submissions
    public List<Quiz> quizzes = new ArrayList<>();
    public List<QuizSubmission> submissions = new ArrayList<>();

    public List<Quiz> findAll() {
        return quizzes;
    }

    public List<QuizSubmission> findAllSubmissions() {
        return submissions;
    }

    public Quiz findById(Long id) {
        for (Quiz quiz : quizzes) {
            if (quiz.getId().equals(id)) {
                return quiz;
            }
        }
        return null;
    }

    public QuizSubmission findQuizSubmissionById(Long id) {
        for (QuizSubmission quiz : submissions) {
            if (quiz.getId().equals(id)) {
                return quiz;
            }
        }
        return null;
    }

    public Quiz save(Quiz quiz) {
        quizzes.add(quiz);
        return quiz;
    }

    public QuizSubmission saveSubmissions(QuizSubmission quizSubmission) {
        submissions.add(quizSubmission);
        return quizSubmission;
    }

    public Quiz update(Quiz quiz) {
        quizzes.remove(quiz);
        quizzes.add(quiz);
        return quiz;
    }

    public void delete(Quiz quiz) {
        quizzes.remove(quiz);
    }

    public void deleteById(Long id) {
        Quiz quiz = findById(id);
        if (quiz != null) {
            quizzes.remove(quiz);
        }
    }
}
