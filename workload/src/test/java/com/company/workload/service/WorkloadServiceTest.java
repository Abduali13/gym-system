package com.company.workload.service;

import com.company.workload.model.WorkloadUpdateRequest;
import com.company.workload.model.TrainerWorkloadSummary;
import com.company.workload.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class WorkloadServiceTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.14");

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private TrainerWorkloadRepository repository;

    private static WorkloadUpdateRequest req(String username, String first, String last, boolean active,
                                             LocalDate date, int duration, WorkloadUpdateRequest.ActionType action) {
        WorkloadUpdateRequest r = new WorkloadUpdateRequest();
        r.setTrainerUsername(username);
        r.setTrainerFirstName(first);
        r.setTrainerLastName(last);
        r.setActive(active);
        r.setTrainingDate(date);
        r.setTrainingDuration(duration);
        r.setAction(action);
        return r;
    }

    @Test
    @DisplayName("Creates new document and sets monthly duration on first event")
    void createNewOnFirstEvent() {
        workloadService.applyUpdate(req("t1", "John", "Doe", true, LocalDate.of(2025, 11, 9), 60, WorkloadUpdateRequest.ActionType.ADD), "tx-1");

        TrainerWorkloadSummary s = repository.findById("t1").orElseThrow();
        assertThat(s.getTrainerFirstName()).isEqualTo("John");
        assertThat(s.getYears().get(2025).get(11)).isEqualTo(60);
        assertThat(workloadService.getMonthly("t1", 2025, 11)).isEqualTo(60);
    }

    @Test
    @DisplayName("Increments and decrements monthly duration, never below zero")
    void incrementDecrement() {
        // Add two sessions
        workloadService.applyUpdate(req("t2", "Ann", "Lee", true, LocalDate.of(2025, 1, 5), 30, WorkloadUpdateRequest.ActionType.ADD), "tx-1");
        workloadService.applyUpdate(req("t2", "Ann", "Lee", true, LocalDate.of(2025, 1, 10), 50, WorkloadUpdateRequest.ActionType.ADD), "tx-2");
        assertThat(workloadService.getMonthly("t2", 2025, 1)).isEqualTo(80);

        // Delete 20
        workloadService.applyUpdate(req("t2", "Ann", "Lee", true, LocalDate.of(2025, 1, 12), 20, WorkloadUpdateRequest.ActionType.DELETE), "tx-3");
        assertThat(workloadService.getMonthly("t2", 2025, 1)).isEqualTo(60);

        // Try to delete more than exists
        workloadService.applyUpdate(req("t2", "Ann", "Lee", true, LocalDate.of(2025, 1, 15), 100, WorkloadUpdateRequest.ActionType.DELETE), "tx-4");
        assertThat(workloadService.getMonthly("t2", 2025, 1)).isEqualTo(0);
    }

    @Test
    @DisplayName("Tracks multiple months/years correctly and updates name/status")
    void multiMonthYearAndNameStatus() {
        workloadService.applyUpdate(req("t3", "Bob", "Ray", false, LocalDate.of(2024, 12, 31), 45, WorkloadUpdateRequest.ActionType.ADD), "tx-1");
        workloadService.applyUpdate(req("t3", "Bob", "Ray", true, LocalDate.of(2025, 1, 1), 15, WorkloadUpdateRequest.ActionType.ADD), "tx-2");

        TrainerWorkloadSummary s = repository.findById("t3").orElseThrow();
        assertThat(s.getYears().get(2024).get(12)).isEqualTo(45);
        assertThat(s.getYears().get(2025).get(1)).isEqualTo(15);
        assertThat(s.getActive()).isTrue();
    }
}
