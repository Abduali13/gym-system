package com.company.gym_system.dao;

import com.company.gym_system.entity.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    Trainee save(Trainee entity);
    Optional<Trainee> findById(Long id);
    List<Trainee> findAll();
    void delete(Long id);
}
