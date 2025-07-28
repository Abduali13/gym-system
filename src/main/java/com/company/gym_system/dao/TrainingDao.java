package com.company.gym_system.dao;

import com.company.gym_system.entity.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingDao {
    Training save(Training entity);
    Optional<Training> findById(Long id);
    List<Training> findAll();
}
