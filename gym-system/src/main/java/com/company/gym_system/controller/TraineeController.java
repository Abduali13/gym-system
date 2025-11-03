package com.company.gym_system.controller;

import com.company.gym_system.dto.*;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Training;
import com.company.gym_system.service.GymFacade;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainees")
public class TraineeController {

    private final GymFacade gymFacade;
    private final TransactionLogger txLogger;

    @PostMapping("/register")
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
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        gymFacade.changeTraineePassword(username, oldPassword, newPassword);
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

    @PutMapping("/update-trainee-trainers")
    public ResponseEntity<List<TrainerListResponseDto>> updateTraineeTrainers(
            @RequestParam String username,
            @RequestParam String password,
            @RequestBody Set<String> trainerUsernames) {
        return ResponseEntity.ok(gymFacade.updateTraineeTrainers(username, password, trainerUsernames));
    }

    @GetMapping("/unassigned-trainees")
    public ResponseEntity<List<Trainee>> findUnassignedTrainees(@RequestParam String username,
                                                                @RequestParam String password) {
        return ResponseEntity.ok(gymFacade.findUnassignedTrainees(username, password));
    }

}
