package com.example.demo.model;
import com.example.demo.model.Student;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String studentId;

    private long quizId;

    private int score;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Answer> answers;

    private boolean requiresManualGrading;

    public QuizSubmission() {}
    public QuizSubmission(String student, long quiz, int score, List<Answer> answers, boolean requiresManualGrading) {
        this.studentId = student;
        this.quizId = quiz;
        this.score = score;
        this.answers = answers;
        this.requiresManualGrading = requiresManualGrading;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStudent() {
        return studentId;
    }
    public void setStudent(String student) {
        this.studentId = student;
    }
    public long getQuiz() {
        return quizId;
    }
    public void setQuiz(long quiz) {
        this.quizId = quiz;
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
    public boolean isRequiresManualGrading() {
        return requiresManualGrading;
    }
    public void setRequiresManualGrading(boolean requiresManualGrading) {
        this.requiresManualGrading = requiresManualGrading;
    }
}