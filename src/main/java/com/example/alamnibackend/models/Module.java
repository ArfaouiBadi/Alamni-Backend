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
public class Module {
    private String title;
    private int duration; // In minutes
    private List<Lesson> lessons;

    @Override
    public String toString() {
        return "Module{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                ", lessons=" + lessons +
                '}';
    }
}