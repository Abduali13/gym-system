package com.company.gym_system.config;


import com.company.gym_system.dao.TraineeDao;
import com.company.gym_system.dao.TrainerDao;
import com.company.gym_system.dao.TrainingDao;
import com.company.gym_system.dao.inmemorydao.InMemoryTraineeDao;
import com.company.gym_system.dao.inmemorydao.InMemoryTrainerDao;
import com.company.gym_system.dao.inmemorydao.InMemoryTrainingDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.company.gym_system")
public class AppConfig {

    @Bean(name="traineeDao")
    public TraineeDao traineeDao() { return new InMemoryTraineeDao(); }

    @Bean(name="trainerDao")
    public TrainerDao trainerDao() { return new InMemoryTrainerDao(); }

    @Bean(name="trainingDao")
    public TrainingDao trainingDao() { return new InMemoryTrainingDao(); }

    @Bean(name="existingUsernames")
    public Map<String, Object> existingUsernames() { return new HashMap<>(); }
}
