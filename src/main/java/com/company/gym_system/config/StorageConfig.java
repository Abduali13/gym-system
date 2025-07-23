package com.company.gym_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StorageConfig {

    @Bean
    public Map<String, Map<Long, Object>> storageMap() {
        return new HashMap<>();
    }

    @Bean(name = "traineeStorage")
    public Map<Long, Object> traineeStorage(Map<String, Map<Long, Object>> storageMap) {
        return storageMap.computeIfAbsent("trainee", key -> new HashMap<>());
    }

    @Bean(name = "trainerStorage")
    public Map<Long, Object> trainerStorage(Map<String, Map<Long, Object>> storageMap) {
        return storageMap.computeIfAbsent("trainer", key -> new HashMap<>());
    }

    @Bean(name = "trainingStorage")
    public Map<Long, Object> trainingStorage(Map<String, Map<Long, Object>> storageMap) {
        return storageMap.computeIfAbsent("training", key -> new HashMap<>());
    }
}
