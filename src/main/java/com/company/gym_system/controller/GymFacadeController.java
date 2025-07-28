package com.company.gym_system.controller;

import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gym")
public class GymFacadeController {
    private final GymFacade gymFacade;

    public GymFacadeController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping("/register-trainee")
    public ResponseEntity<Trainee> registerTrainee(@RequestBody Trainee trainee) {
        Trainee response = gymFacade.registerTrainee(trainee);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-trainer")
    public ResponseEntity<Trainer> registerTrainer(@RequestBody Trainer trainer) {
        Trainer response = gymFacade.registerTrainer(trainer);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/scheduleTraining")
    public ResponseEntity<Training> scheduleTraining(@RequestBody Training training){
        Training response = gymFacade.scheduleTraining(training);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-all-trainees")
    public ResponseEntity<List<Trainee>> listTrainees(){
        List<Trainee> trainees = gymFacade.listAllTrainees();
        return ResponseEntity.ok(trainees);
    }

    @GetMapping("/list-all-trainers")
    public ResponseEntity<List<Trainer>> listTrainers(){
        List<Trainer> trainers = gymFacade.listAllTrainers();
        return ResponseEntity.ok(trainers);
    }

    @GetMapping("/list-all-training")
    public ResponseEntity<List<Training>> listTraining(){
        List<Training> trainings = gymFacade.listAllTrainings();
        return ResponseEntity.ok(trainings);
    }



}