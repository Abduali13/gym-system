package com.company.gym_system.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TrainerShortProfileDto {

    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
}
