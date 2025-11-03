package com.company.gym_system.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TrainerListResponseDto {
    private String firstName;
    private String lastName;
    private String username;
    private String specialization;
}
