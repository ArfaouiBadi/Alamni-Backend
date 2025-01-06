package com.example.alamnibackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Course course;

    private int progress;
    private boolean finished;
    private Date startDate;
    private Date lastVisitedDate;
    private List<String> completedLessons; // List of completed lesson IDs
}