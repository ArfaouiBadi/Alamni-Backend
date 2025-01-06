package com.example.alamnibackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    private String id;
    private String title;
    private String type; // 'Video' or 'PDF'
    private String videoUrl;
    private String pdfUrl;
    private String content;

    @Override
    public String toString() {
        return "Lesson{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", pdfUrl='" + pdfUrl + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}