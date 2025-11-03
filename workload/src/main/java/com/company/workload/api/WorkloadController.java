package com.company.workload.api;

import com.company.workload.model.WorkloadMonthlySummary;
import com.company.workload.model.WorkloadUpdateRequest;
import com.company.workload.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/workloads")
public class WorkloadController {

    private final WorkloadService workloadService;

    @PostMapping
    public ResponseEntity<Void> update(@RequestHeader(value = "X-Transaction-Id", required = false) String txId,
                                       @RequestBody WorkloadUpdateRequest request) {
        String transactionId = txId != null ? txId : UUID.randomUUID().toString();
        log.info("[{}] POST /workloads action={} trainer={} date={} duration={}", transactionId,
                request.getAction(), request.getTrainerUsername(), request.getTrainingDate(), request.getTrainingDuration());
        workloadService.applyUpdate(request, transactionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getMonthly(@RequestHeader(value = "X-Transaction-Id", required = false) String txId,
                                                          @PathVariable String username,
                                                          @RequestParam int year,
                                                          @RequestParam int month) {
        String transactionId = txId != null ? txId : UUID.randomUUID().toString();
        int total = workloadService.getMonthly(username, year, month);
        log.info("[{}] GET /workloads/{}?year={}&month={} -> {}", transactionId, username, year, month, total);
        return ResponseEntity.ok(Map.of("username", username, "year", year, "month", month, "totalDuration", total));
    }

    @GetMapping("/{username}/summary")
    public ResponseEntity<WorkloadMonthlySummary> getSummary(@RequestHeader(value = "X-Transaction-Id", required = false) String txId,
                                                             @PathVariable String username) {
        String transactionId = txId != null ? txId : UUID.randomUUID().toString();
        WorkloadMonthlySummary summary = workloadService.getSummary(username);
        log.info("[{}] GET /workloads/{}/summary -> {}", transactionId, username, summary != null ? "200" : "404");
        if (summary == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(summary);
    }
}
