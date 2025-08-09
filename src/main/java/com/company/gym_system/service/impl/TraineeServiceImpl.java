package com.company.gym_system.service.impl;

import com.company.gym_system.config.AuthGuard;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.User;
import com.company.gym_system.repository.*;
import com.company.gym_system.service.TraineeService;
import com.company.gym_system.service.specs.TrainingSpecs;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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



@Service
@Transactional
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final AuthGuard authGuard;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainingRepository trainingRepository, AuthGuard auth) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.authGuard = auth;
    }

    @Override
    public Trainee create(String fn, String ln, LocalDate birthDate, String address) {

        try {
            Trainee t = new Trainee();
            User user = new User();
            user.setFirstName(fn);
            user.setLastName(ln);
            user.setUsername(generateUsername(fn, ln, this.traineeRepository.existsByUser_Username(fn + ln)));
            user.setPassword(generateRandomPassword());
            t.setUser(user);
            t.setAddress(address);
            t.setBirthDate(birthDate);
            Trainee save = this.traineeRepository.save(t);
            log.info("Trainee has been created successfully");
            return save;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Trainee update(String username, String password, Trainee trainee) {

        try {
            authGuard.checkTrainee(username, password);
            Trainee t = traineeRepository.findByUser_Username(username).get();
            BeanUtils.copyProperties(trainee, t,
                    "id", "username", "password", "isActive");
            log.info("Updated trainee {}", username);
            return traineeRepository.save(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void changePassword(String username, String oldPwd, String newPassword) {
        try {
            authGuard.checkTrainee(username, oldPwd);
            Trainee t = traineeRepository.findByUser_Username(username).get();
            t.getUser().setPassword(newPassword);
            traineeRepository.save(t);
            log.info("Trainee {} changed password", username);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void activate(String username, String password, boolean active) {
        try {
            authGuard.checkTrainee(username, password);
            Trainee t = traineeRepository.findByUser_Username(username).get();
            if (t.getUser().getIsActive().equals(active)) {
                throw new IllegalStateException("Already in that state");
            }
            t.getUser().setIsActive(active);
            traineeRepository.save(t);
            log.info("{} trainee {}", active ? "Activated" : "Deactivated", username);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(String username, String password) {
        try {
            authGuard.checkTrainee(username, password);
            Trainee t = traineeRepository.findByUser_Username(username).get();
            traineeRepository.delete(t);
            log.info("Deleted trainee {}", username);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Trainee findByUsername(String username, String password) {
        try {
            authGuard.checkTrainee(username, password);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        return traineeRepository.findByUser_Username(username).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainings(
            String username, String password, LocalDate from, LocalDate to,
            String trainerName, String trainingType) {
        try {
            authGuard.checkTrainee(username, password);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        Specification<Training> spec = Specification.allOf(
        TrainingSpecs.byTraineeUsername(username),
                TrainingSpecs.byDateRange(from, to),
                TrainingSpecs.byTrainerName(trainerName),
                TrainingSpecs.byTrainingType(trainingType));

        return trainingRepository.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findAvailableTrainers(String username, String password) {
        try {
            authGuard.checkTrainee(username, password);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        Trainee t = traineeRepository.findByUser_Username(username).get();
        return trainerRepository.findAll().stream()
                .filter(tr -> !t.getTrainers().contains(tr))
                .collect(Collectors.toList());
    }

    @Override
    public void updateTrainers(String username, String password, Set<String> trainerUsernames) {
        try {
            authGuard.checkTrainee(username, password);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        Trainee t = traineeRepository.findByUser_Username(username).get();
        List<Trainer> list = trainerUsernames.stream()
                .map(un -> trainerRepository.findByUser_Username(un)
                        .orElseThrow(() -> new EntityNotFoundException(un)))
                .toList();
        t.setTrainers(list);
        traineeRepository.save(t);
        log.info("Updated trainers for {}", username);
    }

    @Override
    public List<Trainee> listAll() {
        return traineeRepository.findAll().stream()
                .filter(t -> t.getUser().getIsActive())
                .collect(Collectors.toList());
    }
}
