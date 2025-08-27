package com.company.gym_system.controller;

import com.company.gym_system.dto.*;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.TrainingType;
import com.company.gym_system.security.AuthGuard;
import com.company.gym_system.service.TraineeService;
import com.company.gym_system.service.TrainerService;
import com.company.gym_system.util.TransactionLogger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/gym")
@RequiredArgsConstructor
public class GymFacadeController {

    private final GymFacade gymFacade;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthGuard authGuard;
    private final TransactionLogger txLogger;

    @PostMapping("/trainee/register")
    public ResponseEntity<TraineeRegistrationResponseDto> registerTrainee(
            @Valid @RequestBody TraineeRegistrationRequestDto request) {
        String txId = txLogger.startTransaction("/trainees/register", request);
        try {
            TraineeRegistrationResponseDto response = gymFacade.registerTrainee(request);
            txLogger.success(txId, response);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            txLogger.error(txId, ex);
            throw ex;
        }
    }

    @GetMapping("/list-all-trainees")
    public ResponseEntity<List<Trainee>> listTrainees() {
        return ResponseEntity.ok(gymFacade.listAllTrainees());
    }


    @PutMapping("/update-trainee")
    public ResponseEntity<TraineeUpdateResponseDto> updateTrainee(
            @RequestBody TraineeUpdateRequestDto updates) throws AccessDeniedException {
        return ResponseEntity.ok(gymFacade.updateTrainee(updates));
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
            @RequestParam String username) {
        return ResponseEntity.ok(gymFacade.getTraineeByUsername(username));
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

    @GetMapping("/trainers/active-unassigned-trainers")
    public ResponseEntity<List<TrainerShortProfileDto>> findAvailableTrainers(
            @RequestParam String username,
            @RequestParam String password) {
        return ResponseEntity.ok(gymFacade.findAvailableTrainers(username, password));
    }

    @PutMapping("/update-trainee-trainers")
    public ResponseEntity<List<TrainerListResponseDto>> updateTraineeTrainers(
            @RequestParam String username,
            @RequestParam String password,
            @RequestBody Set<String> trainerUsernames) {
        return ResponseEntity.ok(gymFacade.updateTraineeTrainers(username, password, trainerUsernames));
    }

    // Trainer endpoints

    @PostMapping("/register-trainer")
    public ResponseEntity<TrainerRegistrationResponseDto> registerTrainee(
            @Valid @RequestBody TrainerRegistrationRequestDto request) {
        String txId = txLogger.startTransaction("/register-trainer", request);
        try {
            TrainerRegistrationResponseDto response = gymFacade.registerTrainer(request);
            txLogger.success(txId, response);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            txLogger.error(txId, ex);
            throw ex;
        }
    }

    @GetMapping("/list-all-trainers")
    public ResponseEntity<List<Trainer>> listTrainers() {
        return ResponseEntity.ok(gymFacade.listAllTrainers());
    }

    @PutMapping("/update-trainer")
    public ResponseEntity<TrainerUpdateResponseDto> updateTrainer(@RequestBody TrainerUpdateRequestDto updates) {
        return ResponseEntity.ok(gymFacade.updateTrainer(updates));
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
    public ResponseEntity<TrainerGetResponseDto> getTrainer(@RequestParam String username,
                                              @RequestParam String password) {
        return ResponseEntity.ok(gymFacade.getTrainerByUsername(username, password));
    }

    @GetMapping("/trainer-trainings")
    public ResponseEntity<List<TrainingGetWithTrainerDto>> getTrainerTrainings(@RequestParam String username,
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

    @PostMapping("/add-training")
    public ResponseEntity<Void> addTraining(@RequestBody Training training) {
        gymFacade.addTraining(training);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list-all-training")
    public ResponseEntity<List<Training>> listTraining() {
        return ResponseEntity.ok(gymFacade.listAllTrainings());
    }


    @GetMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password) {
        String txId = txLogger.startTransaction("/login", username);
        try {
            authGuard.checkAny(username, password);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            txLogger.error(txId, e);
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String username, @RequestParam String newPassword) {
        String txId = txLogger.startTransaction("/reset-password", username);
        try {
            if (traineeService.findByUsername(username) != null) {
                traineeService.changePassword(username, newPassword, newPassword);
            } else if (trainerService.findByUsername(username, newPassword) != null) {
                trainerService.changePassword(username, newPassword, newPassword);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            txLogger.error(txId, e);
            e.printStackTrace();
        }
        txLogger.success(txId, "Password reset for " + username + " new password: " + newPassword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/training-types")
    public ResponseEntity<List<TrainingType>> getTrainingTypes() {
        return ResponseEntity.ok(gymFacade.getTrainingTypes());
    }
}
