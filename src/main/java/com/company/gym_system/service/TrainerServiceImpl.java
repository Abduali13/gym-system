package com.company.gym_system.service;

import com.company.gym_system.dao.GenericDao;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.util.UsernamePasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService{

    private GenericDao<Trainer> trainerDao;
    private Map<String, Object> existingUsernames;

    @Autowired
    public void setTrainerDao(GenericDao<Trainer> trainerDao) {
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

        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setIsActive(true);

        Trainer savedTrainer = trainerDao.save(trainer);
        existingUsernames.put(username, trainer);
        log.info("Trainer with ID {} created.", savedTrainer.getTrainerId());

        return savedTrainer;
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
