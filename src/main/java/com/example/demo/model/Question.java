package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;
    private String questionType;
    private String answer;
    private String options;
    private int marks;

//    @ManyToOne
//    @JoinColumn(name = "quiz_id", nullable = false)
    private Long quizid;

    public Question() {
        super();
    }

    public Question(String questionText, String questionType, String answer, Long quizid) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.answer = answer;
        this.quizid = quizid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getQuiz() {
        return quizid;
    }

    public void setQuiz(Long quizid) {
        this.quizid = quizid;
    }

    public int getMarks() {
        return marks;
    }
    public void setMarks(int marks) {
        this.marks = marks;
    }
    public String getOptions() {
        return options;
    }
    public void setOptions(String options) {
        this.options = options;
    }
}
