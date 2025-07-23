package com.company.gym_system.service;

import com.company.gym_system.entity.Training;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrainingService {
    Training create(Training training);
    List<Training> listAll();
}
