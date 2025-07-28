package com.company.gym_system.dao.impl;

import com.company.gym_system.dao.TrainerDao;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainerDaoImpl implements TrainerDao {

    private Map<Long, Object> store;
    private Long trainerIdGenerator = 0L;
    private final IdGenerator idGenerator;

    @Autowired
    public TrainerDaoImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
    @Autowired
    public void setStore(@Qualifier("trainerStorage") Map<Long, Object> store) {
        this.store = store;
    }

    @Override
    public Trainer save(Trainer trainer) {
        if (trainer.getTrainerId() == null){
            trainer.setTrainerId(trainerIdGenerator++);
        }
        if (trainer.getUserId() == null){
            Long id = idGenerator.nextId();
            trainer.setUserId(id);
        }
        store.put(trainer.getTrainerId(), trainer);
        return trainer;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable((Trainer) store.get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return store.values().stream().filter(Trainer.class::isInstance).map(Trainer.class::cast).toList();
    }

}
