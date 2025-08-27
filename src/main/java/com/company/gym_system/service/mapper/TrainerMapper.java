package com.company.gym_system.service.mapper;


import com.company.gym_system.dto.*;
import com.company.gym_system.entity.Trainer;
import org.springframework.stereotype.Component;


@Component
public class TrainerMapper {

    public TrainerRegistrationResponseDto toDto(Trainer request) {
        TrainerRegistrationResponseDto dto = new TrainerRegistrationResponseDto();
        dto.setUsername(request.getUser().getUsername());
        dto.setPassword(request.getUser().getPassword());
        return dto;
    }

    public TrainerGetResponseDto toGetDto(Trainer trainer) {
        TrainerGetResponseDto dto = new TrainerGetResponseDto();
        dto.setFirstName(trainer.getUser().getFirstName());
        dto.setLastName(trainer.getUser().getLastName());
        dto.setSpecialization(trainer.getSpecialization());
        dto.setTrainees(trainer.getTrainees()
                .stream()
                .map(trainee -> {
            TraineeListResponseDto info = new TraineeListResponseDto();
            info.setFirstName(trainee.getUser().getFirstName());
            info.setLastName(trainee.getUser().getLastName());
            info.setUsername(trainee.getUser().getUsername());
            return info;
        }).toList());
        return dto;
    }

    public TrainerUpdateResponseDto toUpdateDto(Trainer save) {
        if (save == null) {
            return null;
        }
        TrainerUpdateResponseDto dto = new TrainerUpdateResponseDto();
        dto.setUsername(save.getUser().getUsername());
        dto.setFirstName(save.getUser().getFirstName());
        dto.setLastName(save.getUser().getLastName());
        dto.setSpecialization(save.getSpecialization());
        dto.setIsActive(save.getUser().getIsActive());
        dto.setTrainees(save.getTrainees()
                .stream()
                .map(trainee -> {
                    TraineeListResponseDto info = new TraineeListResponseDto();
                    info.setFirstName(trainee.getUser().getFirstName());
                    info.setLastName(trainee.getUser().getLastName());
                    info.setUsername(trainee.getUser().getUsername());
                    return info;
                }).toList());
        return dto;
    }

    public TrainerShortProfileDto toShortProfileDto(Trainer trainer) {
        if (trainer == null) {
            return null;
        }
        TrainerShortProfileDto dto = new TrainerShortProfileDto();
        dto.setFirstName(trainer.getUser().getFirstName());
        dto.setLastName(trainer.getUser().getLastName());
        dto.setSpecialization(trainer.getSpecialization());
        return dto;
    }



}
