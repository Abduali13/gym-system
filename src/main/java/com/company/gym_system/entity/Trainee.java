package com.company.gym_system.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class Trainee extends User{
    private Long traineeId;
    private String address;
    private LocalDate birthDate;
    public Trainee() {}

    public Trainee(Long userId, String firstName, String lastName, String username, String password, Boolean isActive, String address, LocalDate birthDate){
        super(userId, firstName, lastName, username, password, isActive);
        this.address = address;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return traineeId;
    }
}
