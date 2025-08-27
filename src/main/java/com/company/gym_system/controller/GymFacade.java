package com.company.gym_system.controller;

import com.company.gym_system.dto.*;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.TrainingType;
import com.company.gym_system.service.TraineeService;
import com.company.gym_system.service.TrainerService;
import com.company.gym_system.service.TrainingService;
import com.company.gym_system.repository.TrainingTypeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeRepository trainingTypeRepository;

    public TraineeRegistrationResponseDto registerTrainee(@Valid TraineeRegistrationRequestDto request) {
        return traineeService.create(
                request.getFirstName(),
                request.getLastName(),
                request.getBirthDate(),
                request.getAddress()
        );
    }

    public TrainerRegistrationResponseDto registerTrainer(TrainerRegistrationRequestDto trainer) {
        return trainerService.create(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getSpecialization());
    }

    public void addTraining(Training training) {
        trainingService.create(training);
    }

    public List<Trainee> listAllTrainees() {
        return traineeService.listAll();
    }

    public List<Trainer> listAllTrainers() {
        return trainerService.listAll();
    }

    public List<Training> listAllTrainings() {
        return trainingService.listAll();
    }

    public TraineeUpdateResponseDto updateTrainee(TraineeUpdateRequestDto updates) throws AccessDeniedException {
        return traineeService.update(updates);
    }

    public void changeTraineePassword(String username, String oldPwd, String newPwd) {
        traineeService.changePassword(username, oldPwd, newPwd);
    }

    public void activateTrainee(String username, String password, boolean active) {
        traineeService.activate(username, password, active);
    }

    public void deleteTrainee(String username, String password) {
        traineeService.delete(username, password);
    }

    public Trainee getTraineeByUsername(String username) {
        return traineeService.findByUsername(username);
    }

    public List<Training> getTraineeTrainings(String username, String password,
                                              LocalDate from, LocalDate to, String trainerName, String trainingType) {
        return traineeService.getTrainings(username, password, from, to, trainerName, trainingType);
    }

    public List<TrainerShortProfileDto> findAvailableTrainers(String username, String password) {
        return trainerService.findAvailableTrainers(username, password);
    }

    public List<TrainerListResponseDto> updateTraineeTrainers(String username, String password, Set<String> trainerUsernames) {
        return traineeService.updateTrainers(username, password, trainerUsernames);
    }


    public TrainerUpdateResponseDto updateTrainer(TrainerUpdateRequestDto updates) {
        return trainerService.update(updates);
    }

    public void changeTrainerPassword(String username, String oldPwd, String newPwd) {
        trainerService.changePassword(username, oldPwd, newPwd);
    }

    public void activateTrainer(String username, String password, boolean active) {
        trainerService.activate(username, password, active);
    }

    public void deleteTrainer(String username, String password) {
        trainerService.delete(username, password);
    }

    public TrainerGetResponseDto getTrainerByUsername(String username, String password) {
        return trainerService.findByUsername(username, password);
    }

    public List<TrainingGetWithTrainerDto> getTrainerTrainings(String username, String password,
                                              LocalDate from, LocalDate to, String traineeName) {
        return trainerService.getTrainings(username, password, from, to, traineeName);
    }

    public List<Trainee> findUnassignedTrainees(String username, String password) {
        return traineeService.findUnassignedTrainees(username, password);
    }

    public void updateTrainerTrainees(String username, String password, Set<String> traineeUsernames) {
        trainerService.updateTrainees(username, password, traineeUsernames);
    }

    public List<TrainingType> getTrainingTypes() {
        return trainingTypeRepository.findAll();
    }
}
