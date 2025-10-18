package com.company.gym_system.service;

import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.TrainingType;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface TrainingService {
    void create(Training training);
    Training get(Long id);
    List<Training> listAllTrainings();
    void delete(Long id);
    Training addTraining(String username, String password,
                         String traineeUsername, String trainerUsername,
                         LocalDate date, int duration, String type) throws AccessDeniedException;
    List<TrainingType> getTrainingTypes();
}
