package com.company.gym_system.repository;

import com.company.gym_system.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    Optional<Trainee> findByUser_Username(String username);
    boolean existsByUser_Username(String username);
}