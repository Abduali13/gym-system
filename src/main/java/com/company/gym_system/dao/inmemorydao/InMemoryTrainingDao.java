package com.company.gym_system.dao.inmemorydao;


import com.company.gym_system.dao.TrainingDao;
import com.company.gym_system.entity.Training;
import com.company.gym_system.util.IdGenerator;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTrainingDao implements TrainingDao {
    private final Map<Long, Training> store = new ConcurrentHashMap<>();

    @Override 
    public Training save(Training e) {
        Long id = IdGenerator.nextId();
        store.put(id, e);
        return e;
    }

    @Override 
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(store.get(id)); 
    }

    @Override 
    public List<Training> findAll() {
        return new ArrayList<>(store.values()); 
    }

}
