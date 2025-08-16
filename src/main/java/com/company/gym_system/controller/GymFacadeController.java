package com.company.gym_system.controller;

import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/gym")
@RequiredArgsConstructor
public class GymFacadeController {

    private final GymFacade gymFacade;

    @PostMapping("/register-trainee")
    public ResponseEntity<Trainee> registerTrainee(@RequestBody Trainee trainee) {
        return ResponseEntity.ok(gymFacade.registerTrainee(trainee));
    }

    @GetMapping("/list-all-trainees")
    public ResponseEntity<List<Trainee>> listTrainees() {
        return ResponseEntity.ok(gymFacade.listAllTrainees());
    }

    @GetMapping("/list-all-trainers")
    public ResponseEntity<List<Trainer>> listTrainers() {
        return ResponseEntity.ok(gymFacade.listAllTrainers());
    }

    @PutMapping("/update-trainee")
    public ResponseEntity<Trainee> updateTrainee(
            @RequestParam String username,
            @RequestParam String password,
            @RequestBody Trainee updates) throws AccessDeniedException {
        return ResponseEntity.ok(gymFacade.updateTrainee(username, password, updates));
    }

    @PutMapping("/change-trainee-password")
    public ResponseEntity<Void> changeTraineePassword(
            @RequestParam String username,
            @RequestParam String oldPwd,
            @RequestParam String newPwd) {
        gymFacade.changeTraineePassword(username, oldPwd, newPwd);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/activate-trainee")
    public ResponseEntity<Void> activateTrainee(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam boolean active) {
        gymFacade.activateTrainee(username, password, active);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-trainee")
    public ResponseEntity<Void> deleteTrainee(
            @RequestParam String username,
            @RequestParam String password) {
        gymFacade.deleteTrainee(username, password);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainee")
    public ResponseEntity<Trainee> getTraineeByUsername(
            @RequestParam String username,
            @RequestParam String password) {
        return ResponseEntity.ok(gymFacade.getTraineeByUsername(username, password));
    }

    @GetMapping("/trainee-trainings")
    public ResponseEntity<List<Training>> getTraineeTrainings(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType) {
        return ResponseEntity.ok(gymFacade.getTraineeTrainings(username, password, from, to, trainerName, trainingType));
    }

    @GetMapping("/available-trainers")
    public ResponseEntity<List<Trainer>> findAvailableTrainers(
            @RequestParam String username,
            @RequestParam String password) {
        return ResponseEntity.ok(gymFacade.findAvailableTrainers(username, password));
    }

    @PutMapping("/update-trainee-trainers")
    public ResponseEntity<Void> updateTraineeTrainers(
            @RequestParam String username,
            @RequestParam String password,
            @RequestBody Set<String> trainerUsernames) {
        gymFacade.updateTraineeTrainers(username, password, trainerUsernames);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/register-trainer")
    public ResponseEntity<Trainer> registerTrainer(@RequestBody Trainer trainer) {
        return ResponseEntity.ok(gymFacade.registerTrainer(trainer));
    }

    @PutMapping("/update-trainer")
    public ResponseEntity<Trainer> updateTrainer(@RequestParam String username, @RequestParam String password,
                                                 @RequestBody Trainer updates) {
        return ResponseEntity.ok(gymFacade.updateTrainer(username, password, updates));
    }

    @PutMapping("/change-trainer-password")
    public ResponseEntity<Void> changeTrainerPassword(@RequestParam String username,
                                                      @RequestParam String oldPassword,
                                                      @RequestParam String newPassword) {
        gymFacade.changeTrainerPassword(username, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/activate-trainer")
    public ResponseEntity<Void> activateTrainer(@RequestParam String username,
                                                @RequestParam String password,
                                                @RequestParam boolean active) {
        gymFacade.activateTrainer(username, password, active);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-trainer")
    public ResponseEntity<Void> deleteTrainer(@RequestParam String username,
                                              @RequestParam String password) {
        gymFacade.deleteTrainer(username, password);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainer")
    public ResponseEntity<Trainer> getTrainer(@RequestParam String username,
                                              @RequestParam String password) {
        return ResponseEntity.ok(gymFacade.getTrainerByUsername(username, password));
    }

    @GetMapping("/trainer-trainings")
    public ResponseEntity<List<Training>> getTrainerTrainings(@RequestParam String username,
                                                              @RequestParam String password,
                                                              @RequestParam(required = false) LocalDate from,
                                                              @RequestParam(required = false) LocalDate to,
                                                              @RequestParam(required = false) String traineeName) {
        return ResponseEntity.ok(
                gymFacade.getTrainerTrainings(username, password, from, to, traineeName)
        );
    }

    @GetMapping("/unassigned-trainees")
    public ResponseEntity<List<Trainee>> findUnassignedTrainees(@RequestParam String username,
                                                                @RequestParam String password) {
        return ResponseEntity.ok(gymFacade.findUnassignedTrainees(username, password));
    }

    @PutMapping("/update-trainer-trainees")
    public ResponseEntity<Void> updateTrainerTrainees(@RequestParam String username,
                                                      @RequestParam String password,
                                                      @RequestBody Set<String> traineeUsernames) {
        gymFacade.updateTrainerTrainees(username, password, traineeUsernames);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-training")
    public ResponseEntity<Training> scheduleTraining(@RequestBody Training training) {
        return ResponseEntity.ok(gymFacade.scheduleTraining(training));
    }

    @GetMapping("/list-all-training")
    public ResponseEntity<List<Training>> listTraining() {
        return ResponseEntity.ok(gymFacade.listAllTrainings());
    }
}
