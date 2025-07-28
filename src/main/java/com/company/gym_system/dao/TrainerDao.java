package com.company.gym_system.dao;

import com.company.gym_system.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {

    Trainer save(Trainer entity);
    Optional<Trainer> findById(Long id);
    List<Trainer> findAll();

}
