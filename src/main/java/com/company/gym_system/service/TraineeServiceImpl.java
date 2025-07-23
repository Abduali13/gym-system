package com.company.gym_system.service;

import com.company.gym_system.dao.GenericDao;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.util.UsernamePasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {
    private GenericDao<Trainee> traineeDao;
    private Map<String, Object> existingUsernames;

    @Autowired
    public void setTraineeDao(GenericDao<Trainee> traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setExistingUsernames(Map<String, Object> existingUsernames) {
        this.existingUsernames = existingUsernames;
    }

    @Override
    public Trainee create(Trainee trainee) {
        String username = UsernamePasswordUtil.generateUsername(
                trainee.getFirstName(),
                trainee.getLastName(),
                existingUsernames
        );

        String password = UsernamePasswordUtil.generateRandomPassword();

        Trainee savedTrainee = new Trainee();
        savedTrainee.setFirstName(trainee.getFirstName());
        savedTrainee.setLastName(trainee.getLastName());
        savedTrainee.setUsername(username);
        savedTrainee.setPassword(password);
        savedTrainee.setIsActive(true);

        Trainee save = traineeDao.save(savedTrainee);
        existingUsernames.put(username, savedTrainee); // Add to the username map
        log.info("Trainee with ID {} created.", save.getTraineeId());
        return save;
    }

    @Override
    public Trainee update(Trainee trainee) {
        Optional<Trainee> existingTrainee = traineeDao.findById(trainee.getTraineeId());
        if (existingTrainee.isPresent()) {
            Trainee updatedTrainee = traineeDao.save(trainee);
            log.info("Trainee with ID {} updated successfully.", updatedTrainee.getTraineeId());
            return updatedTrainee;
        } else {
            log.error("Failed to update trainee: Trainee with ID {} does not exist.", trainee.getTraineeId());
            throw new IllegalArgumentException("Trainee with ID " + trainee.getTraineeId() + " does not exist.");
        }
    }

    @Override
    public void delete(Long id) {
        try{
            Optional<Trainee> existingTrainee = traineeDao.findById(id);
            traineeDao.delete(id);
            existingTrainee.ifPresent(trainee -> existingUsernames.remove(trainee.getUsername()));
            log.info("Trainee with ID {} deleted successfully.", id);
        }catch (Exception e){
            log.error("Trainee with ID {} could not be deleted.", id);
        }
    }

    @Override
    public List<Trainee> listAll() {
        List<Trainee> trainees = traineeDao.findAll();
        log.info("Retrieved {} trainees from storage.", trainees.size());
        return trainees;
    }

}
