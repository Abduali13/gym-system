package com.company.workload.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkloadUpdateRequest {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean active;
    private LocalDate trainingDate;
    private int trainingDuration;
    private ActionType action;

    public enum ActionType { ADD, DELETE }
}
