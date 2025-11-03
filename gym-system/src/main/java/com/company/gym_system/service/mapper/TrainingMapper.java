package com.company.gym_system.service.mapper;

import com.company.gym_system.dto.TrainingGetWithTrainerDto;
import com.company.gym_system.entity.Training;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public TrainingGetWithTrainerDto toGetWithTrainerDto(Training training) {
        if (training == null) {
            return null;
        }
        TrainingGetWithTrainerDto dto = new TrainingGetWithTrainerDto();
        dto.setTrainingName(training.getTrainingName());
        dto.setTrainingDate(training.getTrainingDate());
        dto.setTrainingDuration(training.getTrainingDuration());
        dto.setTrainer(training.getTrainer());
        dto.setTrainingType(training.getTrainingType());
        return dto;
    }
}
