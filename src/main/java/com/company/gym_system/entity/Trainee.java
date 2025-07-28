package com.company.gym_system.entity;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainee extends User{

    private Long userId;
    private Long traineeId;
    private String address;
    private LocalDate birthDate;


}
