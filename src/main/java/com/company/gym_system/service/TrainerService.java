package com.company.gym_system.service;

import com.company.gym_system.dto.*;
import com.company.gym_system.entity.Trainer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public interface TrainerService {
    TrainerRegistrationResponseDto create(String firstName, String lastName, String specialty);
    TrainerUpdateResponseDto update(TrainerUpdateRequestDto updates);
    void changePassword(String username, String oldPassword, String newPassword);
    void activate(String username, String password, boolean active);
    void delete(String username, String password);
    TrainerGetResponseDto findByUsername(String username, String password);
    List<TrainingGetWithTrainerDto> getTrainings(String username, String password,
                                LocalDate from, LocalDate to, String traineeName);

    List<TrainerShortProfileDto> findAvailableTrainers(String username, String password);
    void updateTrainees(String username, String password, Set<String> traineeUsernames);

    List<Trainer> listAll();
}
