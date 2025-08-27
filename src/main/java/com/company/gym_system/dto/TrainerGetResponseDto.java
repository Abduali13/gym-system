package com.company.gym_system.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainerGetResponseDto {

    private String firstName;
    private String lastName;
    private String specialization;
    private Boolean isActive;
    private List<TraineeListResponseDto> trainees;

}
