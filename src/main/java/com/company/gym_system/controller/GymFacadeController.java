package com.company.gym_system.controller;

import com.company.gym_system.service.TraineeService;
import com.company.gym_system.service.TrainerService;
import com.company.gym_system.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GymFacadeController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacadeController(TraineeService traineeService,
                               TrainerService trainerService,
                               TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }



}
