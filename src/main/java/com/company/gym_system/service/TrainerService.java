package com.company.gym_system.service;

import com.company.gym_system.entity.Trainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrainerService {
    Trainer create(Trainer trainer);
    Trainer update(Trainer trainer);
    List<Trainer> listAll();
}
