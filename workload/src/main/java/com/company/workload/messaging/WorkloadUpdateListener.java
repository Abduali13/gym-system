package com.company.workload.messaging;

import com.company.workload.model.WorkloadUpdateRequest;
import com.company.workload.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkloadUpdateListener {

    private final WorkloadService workloadService;

    @JmsListener(destination = "${app.jms.queue.workload-update:workload.update}")
    public void consume(WorkloadUpdateRequest payload,
                        @Header(name = "X-Transaction-Id", required = false) String txId,
                        @Header(name = "Authorization", required = false) String authorization) { // authorization header can be used for further updates
        String transactionId = txId != null ? txId : UUID.randomUUID().toString();
        log.info("[{}] JMS received workload update: action={} trainer={} date={} duration={}",
                transactionId,
                payload.getAction(),
                payload.getTrainerUsername(),
                payload.getTrainingDate(),
                payload.getTrainingDuration());
        workloadService.applyUpdate(payload, transactionId);
    }
}
