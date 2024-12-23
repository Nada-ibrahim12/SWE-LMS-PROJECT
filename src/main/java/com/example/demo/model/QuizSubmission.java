package com.example.demo.model;
import com.example.demo.model.Student;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    private int score;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Answer> answers;

    private boolean requiresManualGrading;

    public QuizSubmission() {}
    public QuizSubmission(Student student, Quiz quiz, int score, List<Answer> answers, boolean requiresManualGrading) {
        this.student = student;
        this.quiz = quiz;
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
    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
    public Quiz getQuiz() {
        return quiz;
    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
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
