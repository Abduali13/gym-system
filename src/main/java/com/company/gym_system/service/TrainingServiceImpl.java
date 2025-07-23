package com.company.gym_system.service;

import com.company.gym_system.dao.GenericDao;
import com.company.gym_system.entity.Training;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TrainingServiceImpl implements TrainingService{

    private GenericDao<Training> trainingDao;

    @Autowired
    public void setTrainingDao(GenericDao<Training> trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Override
    public Training create(Training training) {
        Training savedTraining = new Training();
        savedTraining.setTrainingDate(training.getTrainingDate());
        savedTraining.setTrainingDuration(training.getTrainingDuration());
        savedTraining.setTrainingName(training.getTrainingName());
        savedTraining.setTrainingType(training.getTrainingType());
        savedTraining.setTrainerId(training.getTrainerId());
        savedTraining.setTraineeId(training.getTraineeId());

        Training save = trainingDao.save(savedTraining);
        log.info("Training with ID {} created.", save.getId());
        return save;
    }

    @Override
    public List<Training> listAll() {
        List<Training> trainings = trainingDao.findAll();
        log.info("Retrieved {} trainings from storage.", trainings.size());
        return trainings;
    }
}
