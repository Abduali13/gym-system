package com.company.workload.repository;

import com.company.workload.model.TrainerWorkloadSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataMongoTest
class TrainerWorkloadRepositoryTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.14");

    @Autowired
    private TrainerWorkloadRepository repository;

    @Test
    @DisplayName("Should save and find by username and by first+last name")
    void saveAndFind() {
        TrainerWorkloadSummary s = TrainerWorkloadSummary.builder()
                .trainerUsername("trainer1")
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .active(true)
                .build();
        repository.save(s);

        assertThat(repository.findByTrainerUsername("trainer1")).isPresent();

        List<TrainerWorkloadSummary> byName = repository.findByTrainerFirstNameAndTrainerLastName("John", "Doe");
        assertThat(byName).hasSize(1);
        assertThat(byName.get(0).getTrainerUsername()).isEqualTo("trainer1");
    }
}
