package com.company.gym_system.config;

import com.company.gym_system.dao.GenericDao;
import com.company.gym_system.dao.InMemoryDao;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.company.gym_system")
public class AppConfig {

    @Bean(name="traineeDao")
    public GenericDao<Trainee> traineeDao() { return new InMemoryDao<>(); }

    @Bean(name="trainerDao")
    public GenericDao<Trainer> trainerDao() { return new InMemoryDao<>(); }

    @Bean(name="trainingDao")
    public GenericDao<Training> trainingDao() { return new InMemoryDao<>(); }

    @Bean(name="existingUsernames")
    public Map<String, Object> existingUsernames() { return new HashMap<>(); }
}
