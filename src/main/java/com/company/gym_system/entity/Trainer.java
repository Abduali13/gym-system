package com.company.gym_system.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trainer extends User{

    private Long trainerId;
    private String specialization;
    private Long userId;

    public Trainer() {}

    public Trainer(Long userId, String firstName, String lastName, String username, String password, Boolean isActive, String specialization, Long trainerId){
        super(userId, firstName, lastName, username, password, isActive);
        this.specialization = specialization;
        this.trainerId = trainerId;
    }

    public Long getId() {
        return trainerId;
    }
}
