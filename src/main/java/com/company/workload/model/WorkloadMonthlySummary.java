package com.company.workload.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadMonthlySummary {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean active;
    private Map<Integer, Map<Integer, Integer>> years;
}
