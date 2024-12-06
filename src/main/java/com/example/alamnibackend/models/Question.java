package com.example.alamnibackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String question;
    private List<String> options;
    private String correctAnswer;

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", options=" + options +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}