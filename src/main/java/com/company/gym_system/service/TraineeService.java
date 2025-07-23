package com.company.gym_system.service;

import com.company.gym_system.entity.Trainee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TraineeService {
    Trainee create(Trainee trainee);
    Trainee update(Trainee trainee);
    void delete(Long id);
    List<Trainee> listAll();
}
