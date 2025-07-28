package com.company.gym_system.dao.impl;

import com.company.gym_system.dao.TraineeDao;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class TraineeDaoImpl implements TraineeDao {

    private Map<Long, Object> store;
    private Long traineeIdGenerator = 0L;
    private final IdGenerator idGenerator;

    @Autowired
    public TraineeDaoImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Autowired
    public void setStore(@Qualifier("traineeStorage") Map<Long, Object> store) {
        this.store = store;
    }

    @Override
    public Trainee save(Trainee trainee) {
        if (trainee.getTraineeId() == null) {
            trainee.setTraineeId(traineeIdGenerator++);
        }
        if (trainee.getUserId() == null){
            Long id = idGenerator.nextId();
            trainee.setUserId(id);
        }
        store.put(trainee.getTraineeId(), trainee);
        return trainee;
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable((Trainee) store.get(id));
    }

    @Override
    public List<Trainee> findAll() {
        List<Trainee> traineeList = new ArrayList<>();
        for (Object value : store.values()) {
            if (value instanceof Trainee) {
                traineeList.add((Trainee) value);
            }
        }
        return traineeList;
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}
