package com.company.gym_system.dao;

import com.company.gym_system.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class TraineeDaoImpl implements GenericDao<Trainee> {

    private Map<Long, Object> store;
    private Long idGenerator = 0L;

    @Autowired
    public void setStore(@Qualifier("traineeStorage") Map<Long, Object> store) {
        this.store = store;
    }

    @Override
    public Trainee save(Trainee trainee) {
        if (trainee.getTraineeId() == null) {
            trainee.setTraineeId(idGenerator++);
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
