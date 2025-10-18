package com.company.workload.service;

import com.company.workload.model.WorkloadMonthlySummary;
import com.company.workload.model.WorkloadUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WorkloadService {

    private final Map<String, WorkloadMonthlySummary> store = new ConcurrentHashMap<>();

    public void applyUpdate(WorkloadUpdateRequest req, String transactionId) {
        store.compute(req.getTrainerUsername(), (username, summary) -> {
            if (summary == null) {
                summary = new WorkloadMonthlySummary();
                summary.setTrainerUsername(req.getTrainerUsername());
                summary.setTrainerFirstName(req.getTrainerFirstName());
                summary.setTrainerLastName(req.getTrainerLastName());
                summary.setActive(req.isActive());
                summary.setYears(new ConcurrentHashMap<>());
            } else {
                summary.setTrainerFirstName(req.getTrainerFirstName());
                summary.setTrainerLastName(req.getTrainerLastName());
                summary.setActive(req.isActive());
            }

            LocalDate d = req.getTrainingDate();
            int year = d.getYear();
            int month = d.getMonthValue();
            summary.getYears().computeIfAbsent(year, y -> new ConcurrentHashMap<>());
            Map<Integer, Integer> months = summary.getYears().get(year);
            months.putIfAbsent(month, 0);
            int delta = req.getAction() == WorkloadUpdateRequest.ActionType.ADD ? req.getTrainingDuration() : -req.getTrainingDuration();
            int newVal = Math.max(0, months.get(month) + delta);
            months.put(month, newVal);

            log.info("[{}] Updated workload for {} Y:{} M:{} by {} -> {}", transactionId, username, year, month, delta, newVal);
            return summary;
        });
    }

    public int getMonthly(String username, int year, int month) {
        WorkloadMonthlySummary s = store.get(username);
        if (s == null) return 0;
        Map<Integer, Integer> months = s.getYears().get(year);
        if (months == null) return 0;
        return months.getOrDefault(month, 0);
    }

    public WorkloadMonthlySummary getSummary(String username) {
        return store.get(username);
    }
}
