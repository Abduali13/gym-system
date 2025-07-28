package com.company.gym_system.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {

    private Long trainingId;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;

    @Enumerated(value = EnumType.STRING)
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private int trainingDuration;

}
