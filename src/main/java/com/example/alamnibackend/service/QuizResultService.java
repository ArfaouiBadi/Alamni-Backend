package com.example.alamnibackend.service;

import com.example.alamnibackend.models.QuizResult;
import com.example.alamnibackend.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizResultService {

    @Autowired
    private QuizResultRepository quizResultRepository;

    public void save(QuizResult quizResult) {
        quizResultRepository.save(quizResult);
    }
}
