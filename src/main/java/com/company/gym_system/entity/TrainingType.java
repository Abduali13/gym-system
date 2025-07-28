package com.company.gym_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum TrainingType {
    CARDIO("Cardio"),
    STRENGTH("Strength"),
    FLEXIBILITY("Flexibility");

    private final String displayName;

}
