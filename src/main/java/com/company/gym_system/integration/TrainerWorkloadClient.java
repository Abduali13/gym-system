package com.company.gym_system.integration;

import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.util.JwtUtil;
import com.company.workload.model.WorkloadUpdateRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainerWorkloadClient {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();

    private String serviceBaseUrl() {
        // For simplicity, call localhost:8081. With Eureka, you could switch to discovery lookup.
        return "http://localhost:8081";
    }

    @CircuitBreaker(name = "workloadService", fallbackMethod = "fallback")
    public void sendUpdate(Training training, WorkloadUpdateRequest.ActionType action, String transactionId) {
        Trainer trainer = training.getTrainer();
        WorkloadUpdateRequest req = new WorkloadUpdateRequest();
        req.setTrainerUsername(trainer.getUser().getUsername());
        req.setTrainerFirstName(trainer.getUser().getFirstName());
        req.setTrainerLastName(trainer.getUser().getLastName());
        req.setActive(Boolean.TRUE.equals(trainer.getUser().getIsActive()));
        req.setTrainingDate(training.getTrainingDate());
        req.setTrainingDuration(training.getTrainingDuration());
        req.setAction(action);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Transaction-Id", transactionId != null ? transactionId : UUID.randomUUID().toString());
        String token = jwtUtil.generateToken("gym-system");
        headers.setBearerAuth(token);

        HttpEntity<WorkloadUpdateRequest> entity = new HttpEntity<>(req, headers);
        String url = serviceBaseUrl() + "/workloads";
        restTemplate.postForEntity(url, entity, Void.class);
        log.info("[{}] Sent {} workload update to {}", headers.getFirst("X-Transaction-Id"), action, url);
    }

    public void fallback(Training training, WorkloadUpdateRequest.ActionType action, String transactionId, Throwable t) {
        log.error("[{}] Workload service call failed for action {}: {}", transactionId, action, t.getMessage());
    }
}
