package com.company.gym_system.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainer extends User{

    private Long trainerId;
    private String specialization;
    private Long userId;

}
