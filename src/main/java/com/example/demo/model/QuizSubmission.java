package com.example.demo.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentId;
    private Long quizId;
    private int score;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Answer> answers;

    public QuizSubmission() {}
    public QuizSubmission(String studentId, Long quizId, int score, List<Answer> answers) {
        this.studentId = studentId;
        this.quizId = quizId;
        this.score = score;
        this.answers = answers;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public Long getQuizId() {
        return quizId;
    }
    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public List<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}

