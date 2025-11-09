package com.company.workload.repository;

import com.company.workload.model.TrainerWorkloadSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerWorkloadRepository extends MongoRepository<TrainerWorkloadSummary, String> {
    Optional<TrainerWorkloadSummary> findByTrainerUsername(String trainerUsername);
    List<TrainerWorkloadSummary> findByTrainerFirstNameAndTrainerLastName(String trainerFirstName, String trainerLastName);
}