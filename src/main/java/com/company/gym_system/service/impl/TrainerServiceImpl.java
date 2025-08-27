package com.company.gym_system.service.impl;

import com.company.gym_system.dto.*;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.User;
import com.company.gym_system.repository.TraineeRepository;
import com.company.gym_system.repository.TrainerRepository;
import com.company.gym_system.repository.TrainingRepository;
import com.company.gym_system.security.AuthGuard;
import com.company.gym_system.service.TrainerService;
import com.company.gym_system.service.mapper.TrainerMapper;
import com.company.gym_system.service.mapper.TrainingMapper;
import com.company.gym_system.service.specs.TrainingSpecs;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.company.gym_system.util.UsernamePasswordUtil.generateRandomPassword;
import static com.company.gym_system.util.UsernamePasswordUtil.generateUsername;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;
    private final AuthGuard authGuard;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;


    @Override
    public TrainerRegistrationResponseDto create(String firstName, String lastName, String specialty) {
        try {
            if (traineeRepository.existsByUser_FirstNameAndUser_LastName(firstName, lastName)) {
                throw new IllegalStateException("User is already registered as trainee");
            }
            Trainer trainer = new Trainer();
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            String baseUsername = firstName + "." + lastName;
            boolean exists = trainerRepository.existsByUser_Username(baseUsername)
                    || traineeRepository.existsByUser_Username(baseUsername);
            user.setUsername(generateUsername(firstName, lastName, exists));
            user.setPassword(generateRandomPassword());
            user.setIsActive(true);
            trainer.setUser(user);
            trainer.setSpecialization(specialty);

            TrainerRegistrationResponseDto dto = this.trainerMapper.toDto(trainerRepository.save(trainer));
            log.info("Trainer has been created successfully");
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TrainerUpdateResponseDto update(TrainerUpdateRequestDto updates) {
        try {
            authGuard.checkTrainer(updates.getUsername(), updates.getPassword());
            Trainer trainer = trainerRepository.findByUser_Username(updates.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException(updates.getUsername()));

            BeanUtils.copyProperties(updates, trainer,
                    "id", "username", "password", "isActive", "specialization");

            log.info("Updated trainer {}", updates.getUsername());
            return this.trainerMapper.toUpdateDto(trainerRepository.save(trainer));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        try {
            authGuard.checkTrainer(username, oldPassword);
            Trainer trainer = trainerRepository.findByUser_Username(username)
                    .orElseThrow(() -> new EntityNotFoundException(username));

            trainer.getUser().setPassword(newPassword);
            trainerRepository.save(trainer);
            log.info("Trainer {} changed password", username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void activate(String username, String password, boolean active) {
        try {
            authGuard.checkTrainer(username, password);
            Trainer trainer = trainerRepository.findByUser_Username(username)
                    .orElseThrow(() -> new EntityNotFoundException(username));

            if (trainer.getUser().getIsActive().equals(active)) {
                throw new IllegalStateException("Already in that state");
            }
            trainer.getUser().setIsActive(active);
            trainerRepository.save(trainer);
            log.info("{} trainer {}", active ? "Activated" : "Deactivated", username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String username, String password) {
        try {
            authGuard.checkTrainer(username, password);
            Trainer trainer = trainerRepository.findByUser_Username(username)
                    .orElseThrow(() -> new EntityNotFoundException(username));

            trainerRepository.delete(trainer);
            log.info("Deleted trainer {}", username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerGetResponseDto findByUsername(String username, String password) {
        try {
            authGuard.checkTrainer(username, password);
        } catch (Exception e) {
            log.error("Access denied for trainer {}", username);
            e.printStackTrace();
        }
        return this.trainerMapper.toGetDto(trainerRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException(username)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingGetWithTrainerDto> getTrainings(String username, String password,
                                                        LocalDate from, LocalDate to, String traineeName) {
        try {
            authGuard.checkTrainer(username, password);
        } catch (Exception e) {
            log.error("Access denied for trainer {}", username);
            e.printStackTrace();
        }

        Specification<Training> spec = Specification.allOf(
                TrainingSpecs.byTrainerUsername(username),
                TrainingSpecs.byDateRange(from, to),
                TrainingSpecs.byTraineeName(traineeName)
        );

        return this.trainingRepository.findAll(spec).stream().map(this.trainingMapper::toGetWithTrainerDto).toList();
    }

    @Override
    public List<TrainerShortProfileDto> findAvailableTrainers(String username, String password) {
        try {
            authGuard.checkTrainee(username, password);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        Trainee t = traineeRepository.findByUser_Username(username).get();
        return this.trainerRepository.findAll().stream()
                .filter(tr -> !t.getTrainers().contains(tr))
                .toList()
                .stream()
                .map(this.trainerMapper::toShortProfileDto)
                .toList();
    }

    @Override
    public void updateTrainees(String username, String password, Set<String> traineeUsernames) {
        try {
            authGuard.checkTrainer(username, password);
        } catch (Exception e) {
            log.error("Access denied for trainer {}", username);
            e.printStackTrace();
        }
        Trainer trainer = trainerRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException(username));

        List<Trainee> trainees = traineeUsernames.stream()
                .map(un -> traineeRepository.findByUser_Username(un)
                        .orElseThrow(() -> new EntityNotFoundException(un)))
                .toList();

        trainer.setTrainees(trainees);
        trainerRepository.save(trainer);
        log.info("Updated trainees for {}", username);
    }

    @Override
    public List<Trainer> listAll() {
        return this.trainerRepository.findAll().stream().filter(t -> t.getUser().getIsActive())
                .collect(Collectors.toList());
    }
}
