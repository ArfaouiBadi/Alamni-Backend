package com.example.alamnibackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private String title;
    private String type; // 'Video' or 'PDF'
    private String videoUrl;
    private String pdfUrl;
    private boolean generateQuiz;
    private String content;

    @Override
    public String toString() {
        return "Lesson{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", pdfUrl='" + pdfUrl + '\'' +
                ", generateQuiz=" + generateQuiz +
                ", content='" + content + '\'' +
                '}';
    }
}