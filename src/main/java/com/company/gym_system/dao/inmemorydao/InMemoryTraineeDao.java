package com.company.gym_system.dao.inmemorydao;

import com.company.gym_system.dao.TraineeDao;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.util.IdGenerator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTraineeDao implements TraineeDao {
    private final Map<Long, Trainee> store = new ConcurrentHashMap<>();

    @Override
    public Trainee save(Trainee e) {
        Long id = IdGenerator.nextId();
        store.put(id, e);
        return e;
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}

