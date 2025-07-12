package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.QuestionBank;

@Repository
public class QuestionBankRepository {

    private List<QuestionBank> questionBanks = new ArrayList<>();

    public List<QuestionBank> findAll() {
        return questionBanks;
    }
    public QuestionBank findById(Long id) {
        for (QuestionBank qb : questionBanks) {
            if (qb.getId().equals(id)) {
                return qb;
            }
        }
        return null;
    }
    public QuestionBank findByCourseId(Long id) {
        for (QuestionBank qb : questionBanks) {
            if (qb.getCourse().getId().equals(id)) {
                return qb;
            }
        }
        return null;
    }

    public QuestionBank save(QuestionBank questionBank) {
        questionBanks.add(questionBank);
        return questionBank;
    }

    public QuestionBank update(QuestionBank questionBank) {
        delete(questionBank); 
        questionBanks.add(questionBank); 
        return questionBank;
    }


    public void delete(QuestionBank questionBank) {
        questionBanks.remove(questionBank);
    }


    public void deleteById(Long id) {
        QuestionBank qb = findById(id);
        if (qb != null) {
            questionBanks.remove(qb);
        }
    }
}

