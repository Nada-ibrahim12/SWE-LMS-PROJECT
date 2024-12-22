package com.example.demo.dto;

import java.util.List;

public class QuizResult {
    private int score;
    private List<String> feedback;

    public QuizResult(int score, List<String> feedback) {
        this.score = score;
        this.feedback = feedback;
    }

    // Getters and Setters
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<String> feedback) {
        this.feedback = feedback;
    }
}

