package com.company.gym_system.integration;

import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.integration.dto.WorkloadUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainerWorkloadClient {

    private final JwtTokenProvider tokenProvider;
    private final JmsTemplate jmsTemplate;

    @Value("${app.jms.queue.workload-update:workload.update}")
    private String workloadUpdateQueue;

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

        String txId = transactionId != null ? transactionId : UUID.randomUUID().toString();
        String token = tokenProvider.generateServiceToken();
        MessagePostProcessor mpp = message -> {
            message.setStringProperty("X-Transaction-Id", txId);
            message.setStringProperty("Authorization", "Bearer " + token);
            return message;
        };
        jmsTemplate.convertAndSend(workloadUpdateQueue, req, mpp);
        log.info("[{}] Sent {} workload update via JMS queue {}", txId, action, workloadUpdateQueue);
    }

}
