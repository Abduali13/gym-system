package com.company.gym_system.service.impl;

import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.User;
import com.company.gym_system.repository.TraineeRepository;
import com.company.gym_system.repository.TrainerRepository;
import com.company.gym_system.repository.TrainingRepository;
import com.company.gym_system.service.AuthGuard;
import com.company.gym_system.service.TrainerService;
import com.company.gym_system.service.specs.TrainingSpecs;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Override
    public Trainer create(String fn, String ln, String specialty) {
        try {
            Trainer trainer = new Trainer();
            User user = new User();
            user.setFirstName(fn);
            user.setLastName(ln);
            user.setUsername(generateUsername(fn, ln, trainerRepository.existsByUser_Username(fn + ln)));
            user.setPassword(generateRandomPassword());
            trainer.setUser(user);
            trainer.setSpecialization(specialty);

            Trainer saved = trainerRepository.save(trainer);
            log.info("Trainer has been created successfully");
            return saved;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Trainer update(String username, String password, Trainer updates) {
        try {
            authGuard.checkTrainer(username, password);
            Trainer trainer = trainerRepository.findByUser_Username(username)
                    .orElseThrow(() -> new EntityNotFoundException(username));

            BeanUtils.copyProperties(updates, trainer,
                    "id", "username", "password", "isActive");

            log.info("Updated trainer {}", username);
            return trainerRepository.save(trainer);
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
    public Trainer findByUsername(String username, String password) {
        try {
            authGuard.checkTrainer(username, password);
        } catch (Exception e) {
            log.error("Access denied for trainer {}", username);
            e.printStackTrace();
        }
        return trainerRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException(username));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainings(String username, String password,
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

        return trainingRepository.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> findUnassignedTrainees(String username, String password) {
        try {
            authGuard.checkTrainer(username, password);
        } catch (Exception e) {
            log.error("Access denied for trainer {}", username);
            e.printStackTrace();
        }
        Trainer trainer = trainerRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException(username));

        return traineeRepository.findAll().stream()
                .filter(trn -> !trainer.getTrainees().contains(trn))
                .collect(Collectors.toList());
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
