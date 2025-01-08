package com.example.alamnibackend.controllers;


import com.example.alamnibackend.models.QuizResult;
import com.example.alamnibackend.service.QuizResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class QuizController {
    @Autowired
    private QuizResultService quizResultService;


    @PostMapping("/submit-score")
    public ResponseEntity<String> submitScore(@RequestBody QuizResult quizResult) {

        quizResultService.save(quizResult);

        return ResponseEntity.ok("Score submitted successfully");
    }
}