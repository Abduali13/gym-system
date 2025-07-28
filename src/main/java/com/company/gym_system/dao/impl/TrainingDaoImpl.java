package com.company.gym_system.dao.impl;

import com.company.gym_system.dao.TrainingDao;
import com.company.gym_system.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainingDaoImpl implements TrainingDao {

    private Map<Long, Object> store;
    private Long idGenerator = 0L;

    @Autowired
    public void setStore(@Qualifier("trainingStorage") Map<Long, Object> store) {
        this.store = store;
    }

    @Override
    public Training save(Training training) {
        if (training.getTrainingId() == null){
            training.setTrainerId(idGenerator++);
        }
        store.put(training.getTrainingId(), training);
        return training;
    }

    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable((Training) store.get(id));
    }

    @Override
    public List<Training> findAll() {
        return store.values().stream().filter(Training.class::isInstance).map(Training.class::cast) .toList();
    }

}
