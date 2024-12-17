package com.example.demo.services;

import com.example.demo.model.Answer;
import com.example.demo.model.Question;
import com.example.demo.model.Quiz;
import com.example.demo.repository.QuizRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailSenderService emailService;

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public void gradeQuiz(Long quizId, List<Answer> answers) {
        Quiz quiz = quizRepository.findById(quizId);

        int totalMarks = 0;
        int obtainedMarks = 0;

        for (Question question : quiz.getQuestions()) {
            totalMarks += question.getMarks();

            Answer studentAnswer = answers.stream()
                    .filter(a -> a.getQuestionId().equals(question.getId()))
                    .findFirst()
                    .orElse(null);

            if (studentAnswer != null) {
                boolean isCorrect = false;

                switch (question.getQuestionType()) {
                    case "MCQ":
                        isCorrect = question.getOptions().equals(studentAnswer.getAnswer());
                        break;
                    case "True/False":
                        isCorrect = question.getAnswer().equals(studentAnswer.getAnswer());
                        break;
                    case "Short Answer":
                        isCorrect = question.getAnswer().equalsIgnoreCase(studentAnswer.getAnswer());
                        break;
                }

                if (isCorrect) {
                    obtainedMarks += question.getMarks();
                }
            }
        }

        double percentage = ((double) obtainedMarks / totalMarks) * 100;
        String feedback = generateFeedback(percentage);

        if (!answers.isEmpty() && answers.get(0).getStudentId() != null) {
            Long studentId = answers.get(0).getStudentId();
        emailService.sendEmail(studentRepository.findById(studentId).getStudentEmail(), "Quiz Grade", feedback);
        }
    }

    private String generateFeedback(double percentage) {
        if (percentage >= 90) {
            return "Excellent work! Keep it up!";
        } else if (percentage >= 75) {
            return "Good job! You have a strong understanding of the material.";
        } else if (percentage >= 50) {
            return "You passed, but there is room for improvement.";
        } else {
            return "You may need to review the material and try again.";
        }
    }

    public List<Question> getRandomizedQuestions(Long quizId, int numQuestions) {
        Quiz quiz = quizRepository.findById(quizId);
        List<Question> questions = quiz.getQuestions();

        Collections.shuffle(questions);

        return questions.subList(0, Math.min(numQuestions, questions.size()));
    }
}
