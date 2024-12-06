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
public class RewardSystem {
    private int points;
    private List<String> badges;
    private int levels;

    @Override
    public String toString() {
        return "RewardSystem{" +
                "points=" + points +
                ", badges=" + badges +
                ", levels=" + levels +
                '}';
    }
}