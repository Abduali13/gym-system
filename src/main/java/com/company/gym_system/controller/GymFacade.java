package com.company.gym_system.controller;

import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.service.TraineeService;
import com.company.gym_system.service.TrainerService;
import com.company.gym_system.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public Trainee registerTrainee(Trainee trainee) {
        return traineeService.create(trainee);
    }

    public Trainer registerTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    public Training scheduleTraining(Training training) {
        return trainingService.create(training);
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
}