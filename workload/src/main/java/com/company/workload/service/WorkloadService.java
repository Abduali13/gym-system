package com.company.workload.service;

import com.company.workload.model.TrainerWorkloadSummary;
import com.company.workload.model.WorkloadUpdateRequest;
import com.company.workload.repository.TrainerWorkloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkloadService {

    private final TrainerWorkloadRepository repository;

    public void applyUpdate(WorkloadUpdateRequest req, String transactionId) {
        String username = req.getTrainerUsername();
        TrainerWorkloadSummary summary = repository.findById(username).orElseGet(() -> {
            TrainerWorkloadSummary s = TrainerWorkloadSummary.builder()
                    .trainerUsername(username)
                    .trainerFirstName(req.getTrainerFirstName())
                    .trainerLastName(req.getTrainerLastName())
                    .active(req.isActive())
                    .years(new HashMap<>())
                    .build();
            log.info("[{}] Creating new workload summary for trainer={}", transactionId, username);
            return s;
        });

        // Always keep name/status up to date
        summary.setTrainerFirstName(req.getTrainerFirstName());
        summary.setTrainerLastName(req.getTrainerLastName());
        summary.setActive(req.isActive());

        LocalDate d = req.getTrainingDate();
        int year = d.getYear();
        int month = d.getMonthValue();
        summary.getYears().computeIfAbsent(year, y -> new HashMap<>());
        Map<Integer, Integer> months = summary.getYears().get(year);
        months.putIfAbsent(month, 0);
        int delta = req.getAction() == WorkloadUpdateRequest.ActionType.ADD ? req.getTrainingDuration() : -req.getTrainingDuration();
        int newVal = Math.max(0, months.get(month) + delta);
        months.put(month, newVal);

        repository.save(summary);
        log.info("[{}] Updated workload for {} Y:{} M:{} by {} -> {}", transactionId, username, year, month, delta, newVal);
    }

    public int getMonthly(String username, int year, int month) {
        return repository.findById(username)
                .map(s -> s.getYears().getOrDefault(year, Map.of()).getOrDefault(month, 0))
                .orElse(0);
    }

    public TrainerWorkloadSummary getSummary(String username) {
        return repository.findById(username).orElse(null);
    }
}
