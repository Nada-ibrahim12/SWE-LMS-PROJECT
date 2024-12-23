package com.example.demo.repository;

import com.example.demo.model.Question;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestionRepository {

    // In-memory storage for questions
    private final List<Question> questions = new ArrayList<>();

    public List<Question> findAll() {
        return new ArrayList<>(questions);
    }

    public Question findById(Long id) {
        return questions.stream()
                .filter(question -> question.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Question> findByQuizId(Long quizId) {
        return questions.stream()
                .filter(question -> question.getQuiz().equals(quizId))
                .toList();
    }

    public Question save(Question question) {
        if (question.getId() == null) {
            question.setId((long) (questions.size() + 1)); // Simple auto-increment for ID
        }
        questions.add(question);
        return question;
    }

    public Question update(Question question) {
        deleteById(question.getId());
        questions.add(question);
        return question;
    }

    public void deleteById(Long id) {
        questions.removeIf(question -> question.getId().equals(id));
    }

    public void deleteAllByQuizId(Long quizId) {
        questions.removeIf(question -> question.getQuiz().equals(quizId));
    }
}
