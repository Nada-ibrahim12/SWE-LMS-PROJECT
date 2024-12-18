package com.example.demo.repository;

import com.example.demo.model.Quiz;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class QuizRepository {

    public List<Quiz> quizzes = new ArrayList<>();

    public List<Quiz> findAll(){
        return quizzes;
    }
    public Quiz findById(Long id){
        for(Quiz quiz : quizzes){
            if(quiz.getId() == id){
                return quiz;
            }
        }
        return null;
    }
    public Quiz save(Quiz quiz){
        quizzes.add(quiz);
        return quiz;
    }
    public Quiz update(Quiz quiz){
        quizzes.remove(quiz);
        quizzes.add(quiz);
        return quiz;
    }
    public void delete(Quiz quiz){
        quizzes.remove(quiz);
    }
    public void deleteById(Long id){
        Quiz quiz = findById(id);
        quizzes.remove(quiz);
    }

}
