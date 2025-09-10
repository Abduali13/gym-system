package com.company.gym_system.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TrainerRegistrationResponseDto {

    private String username;
    private String password; // plain password for initial display only
    private String token;    // JWT for immediate authorization
}
