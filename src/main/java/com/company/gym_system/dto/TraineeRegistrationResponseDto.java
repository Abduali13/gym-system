package com.company.gym_system.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TraineeRegistrationResponseDto {

    private String username;
    private String password;
}
