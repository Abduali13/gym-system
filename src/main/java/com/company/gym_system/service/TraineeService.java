package com.company.gym_system.service;

import com.company.gym_system.dto.*;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Training;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public interface TraineeService {
    TraineeRegistrationResponseDto create(String firstName, String lastName, LocalDate birthDate, String address);
    TraineeUpdateResponseDto updateTrainee(TraineeUpdateRequestDto updates) throws AccessDeniedException;
    void changeTraineePassword(String username, String oldPassword, String newPassword);
    void activateTrainee(String username, String password, boolean active);
    void deleteTrainee(String username, String password);
    Trainee getTraineeByUsername(String username);
    List<Training> getTraineeTrainings(String username, String password,
                                       LocalDate from, LocalDate to, String trainerName, String trainingType);
    List<Trainee> findUnassignedTrainees(String username, String password);
    List<TrainerListResponseDto> updateTraineeTrainers(String username, String password, Set<String> trainerUsernames);

    List<Trainee> listAllTrainees();
}
