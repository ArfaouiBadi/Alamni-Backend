package com.example.alamnibackend.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String id;
    private String imageUrl;
    private String title;
    private String description;
    private int levelRequired;
    private int duration; // In minutes
    private String category;
    private List<Module> modules;
    private RewardSystem rewardSystem;

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", levelRequired=" + levelRequired +
                ", duration=" + duration +
                ", category='" + category + '\'' +
                ", modules=" + modules +
                ", rewardSystem=" + rewardSystem +
                '}';
    }
}