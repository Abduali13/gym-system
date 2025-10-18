package com.company.gym_system.service.impl;

import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.TrainingType;
import com.company.gym_system.repository.TraineeRepository;
import com.company.gym_system.repository.TrainerRepository;
import com.company.gym_system.repository.TrainingRepository;
import com.company.gym_system.repository.TrainingTypeRepository;
import com.company.gym_system.security.AuthGuard;
import com.company.gym_system.service.TrainingService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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
    private final MeterRegistry meterRegistry;
    private final com.company.gym_system.integration.TrainerWorkloadClient workloadClient;

    @Override
    public void create(Training training) {
        String txId = java.util.UUID.randomUUID().toString();
        log.info("[{}] Creating training: {}", txId, training);
        trainingRepository.save(training);
        meterRegistry.counter("trainings_created_total").increment();
        log.info("[{}] Training created successfully with id: {}", txId, training.getTrainingId());
        try {
            workloadClient.sendUpdate(training, com.company.workload.model.WorkloadUpdateRequest.ActionType.ADD, txId);
        } catch (Exception e) {
            log.error("[{}] Failed to notify workload service for ADD: {}", txId, e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Training get(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> listAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Training addTraining(
            String username, String password,
            String traineeUsername, String trainerUsername,
            LocalDate date, int duration, String type) throws AccessDeniedException {

        authGuard.checkTrainee(username, password);

        Timer.Sample sample = Timer.start();
        try {
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
            meterRegistry.counter("trainings_added_total").increment();
            log.info("Added training {} for trainee {} with trainer {}", saved.getTrainingId(), trainee.getUser().getUsername(), trainer.getUser().getUsername());
            return saved;
        } finally {
            sample.stop(Timer.builder("training_add_duration_seconds")
                    .description("Time taken to add a training")
                    .register(meterRegistry));
        }
    }

    @Override
    public List<TrainingType> getTrainingTypes() {
        return trainingTypeRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        String txId = java.util.UUID.randomUUID().toString();
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found with id " + id));
        trainingRepository.deleteById(id);
        meterRegistry.counter("trainings_deleted_total").increment();
        log.info("[{}] Training deleted id: {}", txId, id);
        try {
            workloadClient.sendUpdate(training, com.company.workload.model.WorkloadUpdateRequest.ActionType.DELETE, txId);
        } catch (Exception e) {
            log.error("[{}] Failed to notify workload service for DELETE: {}", txId, e.getMessage());
        }
    }
}
