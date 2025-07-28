package com.company.gym_system.service.impl;

import com.company.gym_system.dao.TrainerDao;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.service.TrainerService;
import com.company.gym_system.util.UsernamePasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private TrainerDao trainerDao;
    private Map<String, Object> existingUsernames;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }
    @Autowired
    public void setExistingUsernames(Map<String, Object> existingUsernames) {
        this.existingUsernames = existingUsernames;
    }

    @Override
    public Trainer create(Trainer trainer) {
        String username = UsernamePasswordUtil.generateUsername(
                trainer.getFirstName(),
                trainer.getLastName(),
                existingUsernames
        );

        String password = UsernamePasswordUtil.generateRandomPassword();

        Trainer savedTrainer = new Trainer();
        savedTrainer.setFirstName(trainer.getFirstName());
        savedTrainer.setLastName(trainer.getLastName());
        savedTrainer.setUsername(username);
        savedTrainer.setPassword(password);
        savedTrainer.setIsActive(true);
        savedTrainer.setSpecialization(savedTrainer.getSpecialization());

        Trainer save = trainerDao.save(savedTrainer);
        existingUsernames.put(username, save);
        log.info("Trainer with ID {} created.", save.getTrainerId());

        return save;
    }

    @Override
    public Trainer update(Trainer trainer) {
        Optional<Trainer> existingTrainer = trainerDao.findById(trainer.getTrainerId());
        if (existingTrainer.isPresent()) {
            Trainer updatedTrainer = trainerDao.save(trainer);
            log.info("Trainer with ID {} updated successfully.", updatedTrainer.getTrainerId());
            return updatedTrainer;
        }
        else {
            log.error("Failed to update trainer: Trainer with ID {} does not exist.", trainer.getTrainerId());
            throw new IllegalArgumentException("Trainer with ID " + trainer.getTrainerId() + " does not exist.");
        }
    }

    @Override
    public List<Trainer> listAll() {
        List<Trainer> trainers = trainerDao.findAll();
        log.info("Retrieved {} trainers from storage.", trainers.size());
        return trainers;
    }

}
