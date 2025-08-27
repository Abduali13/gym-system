package com.company.gym_system.security;

import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.repository.TraineeRepository;
import com.company.gym_system.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class AuthGuard {
    private final TraineeRepository teRepo;
    private final TrainerRepository trRepo;

    @Autowired
    public AuthGuard(TraineeRepository teRepo, TrainerRepository trRepo) {
        this.teRepo = teRepo;
        this.trRepo = trRepo;
    }

    public void checkTrainee(String username, String password) throws AccessDeniedException {
        Trainee t = teRepo.findByUser_Username(username)
                .orElseThrow(() -> new AccessDeniedException("Invalid trainee"));
        if (!t.getUser().getPassword().equals(password) || !t.getUser().getIsActive()) {
            throw new AccessDeniedException("Bad credentials or inactive");
        }
    }

    public void checkTrainer(String username, String password) throws AccessDeniedException {
        Trainer t = trRepo.findByUser_Username(username)
                .orElseThrow(() -> new AccessDeniedException("Invalid trainer"));
        if (!t.getUser().getPassword().equals(password) || !t.getUser().getIsActive()) {
            throw new AccessDeniedException("Bad credentials or inactive");
        }
    }

    public void checkAny(String username, String password) throws AccessDeniedException {
        var traineeOpt = teRepo.findByUser_Username(username);
        var trainerOpt = trRepo.findByUser_Username(username);

        if (traineeOpt.isPresent()) {
            Trainee t = traineeOpt.get();
            if (Boolean.TRUE.equals(t.getUser().getIsActive()) && t.getUser().getPassword().equals(password)) {
                return; // authenticated as trainee
            }
        }
        if (trainerOpt.isPresent()) {
            Trainer t = trainerOpt.get();
            if (Boolean.TRUE.equals(t.getUser().getIsActive()) && t.getUser().getPassword().equals(password)) {
                return; // authenticated as trainer
            }
        }
        throw new AccessDeniedException("Invalid credentials or inactive");
    }


}
