package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private String answer;  // Student's answer

    @ManyToOne
    @JoinColumn(name = "quiz_submission_id", nullable = false)
    private QuizSubmission quizSubmission;

    private boolean isCorrect;

    private int score;

    private String submittedAnswer;


    public Answer() {}

    public Answer(Question question, String answer, int score) {
        this.question = question;
        this.answer = answer;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
    public QuizSubmission getQuizSubmission() {
        return quizSubmission;
    }
    public void setQuizSubmission(QuizSubmission quizSubmission) {
        this.quizSubmission = quizSubmission;
    }
    public boolean isCorrect() {
        return isCorrect;
    }
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getSubmittedAnswer() {
        return submittedAnswer;
    }
    public void setSubmittedAnswer(String submittedAnswer) {
        this.submittedAnswer = submittedAnswer;
    }

}

