package com.example.demo.services;

import com.example.demo.model.*;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.QuizRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.QuestionBankRepository;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    public Quiz createQuiz(Long courseId, Quiz quiz) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        quiz.setCourse(course);

        for (Question question : quiz.getQuestions()) {
            question.setQuiz(quiz.getId());
        }

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

    public List<Question> getRandomizedQuestions(Long questionBankId, int numQuestions) {
        QuestionBank questionBank = questionBankRepository.findById(questionBankId);
        List<Question> questions = questionBank.getQuestions();

        Collections.shuffle(questions);

        return questions.subList(0, Math.min(numQuestions, questions.size()));
    }

    public QuizSubmission submitQuiz(QuizSubmission quizSubmission) {
        Quiz quiz = quizRepository.findById(quizSubmission.getQuiz().getId());

        int totalMarks = 0;
        int obtainedMarks = 0;
        boolean requiresManualGrading = false;

        for (Question question : quiz.getQuestions()) {
            totalMarks += question.getMarks();
        }

        for (Answer answer : quizSubmission.getAnswers()) {
            Question question = quiz.getQuestions().stream()
                    .filter(q -> q.getId().equals(answer.getQuestion().getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            boolean isCorrect = false;

            switch (question.getQuestionType()) {
                case "MCQ":
                    isCorrect = question.getOptions().equals(answer.getAnswer());
                    break;
                case "True/False":
                    isCorrect = question.getAnswer().equals(answer.getAnswer());
                    break;
                case "Short Answer":
                    isCorrect = question.getAnswer().equalsIgnoreCase(answer.getAnswer());
                    break;
            }

            if (isCorrect) {
                obtainedMarks += question.getMarks();
            } else {
                requiresManualGrading = true;
            }
        }

        double percentage = ((double) obtainedMarks / totalMarks) * 100;
        String feedback = generateFeedback(percentage);
        quizSubmission.setScore(obtainedMarks);
        quizSubmission.setRequiresManualGrading(requiresManualGrading);

        String studentId = quizSubmission.getStudent().getUserId();
        emailService.sendEmail(
                studentRepository.findById(studentId).getStudentEmail(),
                "Quiz Grade",
                feedback
        );

        return quizRepository.saveSubmissions(quizSubmission);
    }

    public QuizSubmission findQuizSubmissionById(Long submissionId) {
        return quizRepository.findQuizSubmissionById(submissionId);
    }

    public QuizSubmission updateQuizSubmission(QuizSubmission submission) {
        quizRepository.saveSubmissions(submission);
        return submission;
    }
}
