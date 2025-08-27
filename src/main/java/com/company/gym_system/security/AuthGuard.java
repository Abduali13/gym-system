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
        Trainee trainee = teRepo.findByUser_Username(username)
                .orElseThrow(() -> new AccessDeniedException("Invalid trainee"));
        Trainer trainer = trRepo.findByUser_Username(username)
                .orElseThrow(() -> new AccessDeniedException("Invalid trainer"));
        if (!trainee.getUser().getPassword().equals(password) || !trainee.getUser().getIsActive()) {
            throw new AccessDeniedException("Bad credentials or inactive");
        }
        if (!trainer.getUser().getPassword().equals(password) || !trainer.getUser().getIsActive()) {
            throw new AccessDeniedException("Bad credentials or inactive");
        }
    }


}
