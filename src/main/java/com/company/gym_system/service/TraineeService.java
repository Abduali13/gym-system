package com.company.gym_system.service;

import com.company.gym_system.dto.*;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public interface TraineeService {
    TraineeRegistrationResponseDto create(String firstName, String lastName, LocalDate birthDate, String address);
    TraineeUpdateResponseDto update(TraineeUpdateRequestDto updates) throws AccessDeniedException;
    void changePassword(String username, String oldPassword, String newPassword);
    void activate(String username, String password, boolean active);
    void delete(String username, String password);
    Trainee findByUsername(String username);
    List<Training> getTrainings(String username, String password,
                                LocalDate from, LocalDate to, String trainerName, String trainingType);
    List<Trainee> findUnassignedTrainees(String username, String password);
    List<TrainerListResponseDto> updateTrainers(String username, String password, Set<String> trainerUsernames);

    List<Trainee> listAll();
}
