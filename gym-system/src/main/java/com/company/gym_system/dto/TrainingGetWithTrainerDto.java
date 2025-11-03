package com.company.gym_system.dto;

import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.TrainingType;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TrainingGetWithTrainerDto {
    private String trainingName;
    private LocalDate trainingDate;
    private int trainingDuration;
    private Trainer trainer;
    private TrainingType trainingType;

}
