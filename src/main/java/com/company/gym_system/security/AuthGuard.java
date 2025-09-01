package com.company.gym_system.security;

import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.repository.TraineeRepository;
import com.company.gym_system.repository.TrainerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Slf4j
@Component
public class AuthGuard {
    private final TraineeRepository teRepo;
    private final TrainerRepository trRepo;
    private final MeterRegistry meterRegistry;

    @Autowired
    public AuthGuard(TraineeRepository teRepo, TrainerRepository trRepo, MeterRegistry meterRegistry) {
        this.teRepo = teRepo;
        this.trRepo = trRepo;
        this.meterRegistry = meterRegistry;
    }

    public void checkTrainee(String username, String password) throws AccessDeniedException {
        try {
            Trainee t = teRepo.findByUser_Username(username)
                    .orElseThrow(() -> new AccessDeniedException("Invalid trainee"));
            if (!t.getUser().getPassword().equals(password) || !t.getUser().getIsActive()) {
                throw new AccessDeniedException("Bad credentials or inactive");
            }
            meterRegistry.counter("auth_attempts_total", "result", "success", "type", "trainee").increment();
            log.info("Trainee {} authenticated", username);
        } catch (AccessDeniedException ex) {
            meterRegistry.counter("auth_attempts_total", "result", "failure", "type", "trainee").increment();
            log.warn("Trainee auth failed for {}: {}", username, ex.getMessage());
            throw ex;
        }
    }

    public void checkTrainer(String username, String password) throws AccessDeniedException {
        try {
            Trainer t = trRepo.findByUser_Username(username)
                    .orElseThrow(() -> new AccessDeniedException("Invalid trainer"));
            if (!t.getUser().getPassword().equals(password) || !t.getUser().getIsActive()) {
                throw new AccessDeniedException("Bad credentials or inactive");
            }
            meterRegistry.counter("auth_attempts_total", "result", "success", "type", "trainer").increment();
            log.info("Trainer {} authenticated", username);
        } catch (AccessDeniedException ex) {
            meterRegistry.counter("auth_attempts_total", "result", "failure", "type", "trainer").increment();
            log.warn("Trainer auth failed for {}: {}", username, ex.getMessage());
            throw ex;
        }
    }

    public void checkAny(String username, String password) throws AccessDeniedException {
        var traineeOpt = teRepo.findByUser_Username(username);
        var trainerOpt = trRepo.findByUser_Username(username);

        if (traineeOpt.isPresent()) {
            Trainee t = traineeOpt.get();
            if (Boolean.TRUE.equals(t.getUser().getIsActive()) && t.getUser().getPassword().equals(password)) {
                meterRegistry.counter("auth_attempts_total", "result", "success", "type", "any").increment();
                log.info("Authenticated as trainee {}", username);
                return; // authenticated as trainee
            }
        }
        if (trainerOpt.isPresent()) {
            Trainer t = trainerOpt.get();
            if (Boolean.TRUE.equals(t.getUser().getIsActive()) && t.getUser().getPassword().equals(password)) {
                meterRegistry.counter("auth_attempts_total", "result", "success", "type", "any").increment();
                log.info("Authenticated as trainer {}", username);
                return; // authenticated as trainer
            }
        }
        meterRegistry.counter("auth_attempts_total", "result", "failure", "type", "any").increment();
        log.warn("Auth failed for {} (any)", username);
        throw new AccessDeniedException("Invalid credentials or inactive");
    }


}
