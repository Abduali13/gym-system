package com.company.gym_system.controller;

import com.company.gym_system.dto.*;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.security.AuthGuard;
import com.company.gym_system.service.GymFacade;
import com.company.gym_system.util.TransactionLogger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainers")
public class TrainerController {

    private final GymFacade gymFacade;
    private final TransactionLogger txLogger;

    @PostMapping("/register")
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


    @PutMapping("/update-trainer-trainees")
    public ResponseEntity<Void> updateTrainerTrainees(@RequestParam String username,
                                                      @RequestParam String password,
                                                      @RequestBody Set<String> traineeUsernames) {
        gymFacade.updateTrainerTrainees(username, password, traineeUsernames);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active-unassigned-trainers")
    public ResponseEntity<List<TrainerShortProfileDto>> findAvailableTrainers(
            @RequestParam String username,
            @RequestParam String password) {
        return ResponseEntity.ok(gymFacade.findAvailableTrainers(username, password));
    }
}
