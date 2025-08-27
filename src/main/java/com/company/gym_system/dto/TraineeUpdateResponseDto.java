package com.company.gym_system.dto;

import lombok.Data;


import java.time.LocalDate;
import java.util.List;

@Data
public class TraineeUpdateResponseDto {

    private String firstName;
    private String lastName;
    private String username;
    private Boolean isActive;
    private String address;
    private LocalDate birthDate;
    private List<TrainerListResponseDto> trainers;

}
