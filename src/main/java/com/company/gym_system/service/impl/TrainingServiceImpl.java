package com.company.gym_system.service.impl;

import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.TrainingType;
import com.company.gym_system.repository.TraineeRepository;
import com.company.gym_system.repository.TrainerRepository;
import com.company.gym_system.repository.TrainingRepository;
import com.company.gym_system.repository.TrainingTypeRepository;
import com.company.gym_system.service.AuthGuard;
import com.company.gym_system.service.TrainingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final AuthGuard authGuard;

    @Override
    public Training create(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    @Transactional(readOnly = true)
    public Training get(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> listAll() {
        return trainingRepository.findAll();
    }

    @Override
    public Training addTraining(
            String username, String password,
            String traineeUsername, String trainerUsername,
            LocalDate date, int duration, String type) {

        try {
            authGuard.checkTrainee(username, password);
        } catch (AccessDeniedException e) {
            log.error("Access denied for user {}: {}", username, e.getMessage());
            e.printStackTrace();
        }

        Trainer trainer = trainerRepository.findByUser_Username(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + trainerUsername));
        Trainee trainee = traineeRepository.findByUser_Username(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + traineeUsername));
        TrainingType trainingType = trainingTypeRepository.findByTrainingTypeName(type)
                .orElseThrow(() -> new EntityNotFoundException("Training type not found: " + type));

        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingDate(date);
        training.setTrainingDuration(duration);
        training.setTrainingType(trainingType);

        Training saved = trainingRepository.save(training);
        log.info("Added training {} for trainee {} with trainer {}", saved.getTrainingId(), trainee.getUser().getUsername(), trainer.getUser().getUsername());
        return saved;
    }
}
