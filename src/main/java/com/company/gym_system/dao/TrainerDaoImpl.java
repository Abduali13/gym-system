package com.company.gym_system.dao;

import com.company.gym_system.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public abstract class TrainerDaoImpl implements GenericDao<Trainer> {

    private Map<Long, Object> store;
    private Long idGenerator = 0L;

    @Autowired
    public void setStore(@Qualifier("trainerStorage") Map<Long, Object> store) {
        this.store = store;
    }

    @Override
    public Trainer save(Trainer trainer) {
        if (trainer.getTrainerId() == null){
            trainer.setTrainerId(idGenerator++);
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
