package com.company.gym_system.service;

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
    Trainee create(String fn, String ln, LocalDate birthDate, String address);
    Trainee update(String username, String password, Trainee updates) throws AccessDeniedException;
    void changePassword(String username, String oldPwd, String newPassword);
    void activate(String username, String password, boolean active);
    void delete(String username, String password);
    Trainee findByUsername(String username, String password);
    List<Training> getTrainings(String username, String password,
                                LocalDate from, LocalDate to, String trainerName, String trainingType);
    List<Trainer> findAvailableTrainers(String username, String password);
    void updateTrainers(String username, String password, Set<String> trainerUsernames);

    List<Trainee> listAll();
}
