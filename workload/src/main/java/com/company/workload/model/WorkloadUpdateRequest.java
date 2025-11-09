package com.company.workload.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkloadUpdateRequest {
    @NotBlank
    private String trainerUsername;
    @NotBlank
    private String trainerFirstName;
    @NotBlank
    private String trainerLastName;
    private boolean active;
    @NotNull
    private LocalDate trainingDate;
    @Positive
    private int trainingDuration;
    @NotNull
    private ActionType action;

    public enum ActionType { ADD, DELETE }
}
