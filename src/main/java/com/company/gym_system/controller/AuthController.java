package com.company.gym_system.controller;

import com.company.gym_system.dto.TrainerGetResponseDto;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.security.AuthGuard;
import com.company.gym_system.service.TraineeService;
import com.company.gym_system.service.TrainerService;
import com.company.gym_system.util.TransactionLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthGuard authGuard;
    private final TransactionLogger txLogger;

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
            Trainee trainee = traineeService.getTraineeByUsername(username);
            if (trainee != null) {
                traineeService.changeTraineePassword(username, newPassword, newPassword);
            } else {
                TrainerGetResponseDto trainer = trainerService.getTrainerByUsername(username, newPassword);
                if (trainer != null) {
                    trainerService.changeTrainerPassword(username, newPassword, newPassword);
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        } catch (Exception e) {
            txLogger.error(txId, e);
        }
        txLogger.success(txId, "Password reset for " + username);
        return ResponseEntity.ok().build();
    }
}
