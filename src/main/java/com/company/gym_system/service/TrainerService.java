package com.company.gym_system.service;

import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public interface TrainerService {
    Trainer create(String firstName, String lastName, String specialty);
    Trainer update(String username, String password, Trainer updates);
    void changePassword(String username, String oldPassword, String newPassword);
    void activate(String username, String password, boolean active);
    void delete(String username, String password);
    Trainer findByUsername(String username, String password);
    List<Training> getTrainings(String username, String password,
                                LocalDate from, LocalDate to, String traineeName);
    List<Trainee> findUnassignedTrainees(String username, String password);
    void updateTrainees(String username, String password, Set<String> traineeUsernames);

    List<Trainer> listAll();
}
