package com.example.alamnibackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Document(collection = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String id;

    @NotBlank
    private String imageUrl;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String description;


    @Min(1)
    @Max(100)
    private int levelRequired;
    @Min(1)
    @Max(10000)
    private int pointsRequired;

    @Min(1)
    private int duration; // In minutes

    @DBRef(lazy = false)
    private Category category;

    private List<Module> modules;
    private RewardSystem rewardSystem;

    @DBRef(lazy = false)
    private Set<Enrollment> enrollments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", levelRequired=" + levelRequired +
                ", pointsRequired=" + pointsRequired +
                ", duration=" + duration +
                ", category=" + category +
                ", modules=" + modules +
                ", rewardSystem=" + rewardSystem +
                ", enrollments=" + enrollments +
                '}';
    }
}