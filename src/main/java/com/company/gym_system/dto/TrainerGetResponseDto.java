package com.company.gym_system.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TrainerGetResponseDto {

    private String firstName;
    private String lastName;
    private String specialization;
    private Boolean isActive;
    private List<TraineeListResponseDto> trainees;

}
