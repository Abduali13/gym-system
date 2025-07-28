package com.company.gym_system.service.impl;

import com.company.gym_system.dao.TrainingDao;
import com.company.gym_system.entity.Training;
import com.company.gym_system.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TrainingServiceImpl implements TrainingService {

    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
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

        log.info("Training with ID {} created.", save.getTrainingId());
        return save;
    }

    @Override
    public List<Training> listAll() {
        List<Training> trainings = trainingDao.findAll();
        log.info("Retrieved {} trainings from storage.", trainings.size());
        return trainings;
    }
}
