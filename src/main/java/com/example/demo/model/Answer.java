package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("questionId")
    private Long questionId;

    private String answer;  // correct answer

    private Long quizSubmissionId;

    private boolean isCorrect;

    private int score;

    private String submittedAnswer;


    public Answer() {}

    public Answer(Long question, String answer, int score) {
        this.questionId = question;
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
    public Long getQuestion() {
        return questionId;
    }
    public void setQuestion(Long question) {
        this.questionId = question;
    }
    public Long getQuizSubmission() {
        return quizSubmissionId;
    }
    public void setQuizSubmission(Long quizSubmission) {
        this.quizSubmissionId = quizSubmission;
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

