package com.example.alamnibackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "quizResults")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResult {
    private String username;
    private double score;

}