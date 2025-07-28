package com.company.gym_system.dao.inmemorydao;

import com.company.gym_system.dao.TrainerDao;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.util.IdGenerator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTrainerDao implements TrainerDao {

    private final Map<Long, Trainer> store = new ConcurrentHashMap<>();

    @Override
    public Trainer save(Trainer e) {
        Long id = IdGenerator.nextId();
        store.put(id, e);
        return e;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(store.values());
    }

}
