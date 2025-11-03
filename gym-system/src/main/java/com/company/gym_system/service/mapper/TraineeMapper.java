package com.company.gym_system.service.mapper;

import com.company.gym_system.dto.TraineeRegistrationResponseDto;
import com.company.gym_system.dto.TraineeUpdateResponseDto;
import com.company.gym_system.dto.TrainerListResponseDto;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapper {

    public TraineeRegistrationResponseDto toRegistrationDto(Trainee trainee) {
        if (trainee == null) {
            return null;
        }
        TraineeRegistrationResponseDto dto = new TraineeRegistrationResponseDto();
        dto.setUsername(trainee.getUser().getUsername());
        dto.setPassword(trainee.getUser().getPassword());
        return dto;
    }

    public TraineeUpdateResponseDto toUpdateDto(Trainee trainee) {
        if (trainee == null) {
            return null;
        }
        TraineeUpdateResponseDto dto = new TraineeUpdateResponseDto();
        dto.setUsername(trainee.getUser().getUsername());
        dto.setFirstName(trainee.getUser().getFirstName());
        dto.setLastName(trainee.getUser().getLastName());
        dto.setBirthDate(trainee.getBirthDate());
        dto.setAddress(trainee.getAddress());
        dto.setIsActive(trainee.getUser().getIsActive());
        dto.setTrainers(trainee.getTrainers().stream()
                .map(trainer -> {
                    TrainerListResponseDto response = new TrainerListResponseDto();
                    response.setFirstName(trainer.getUser().getFirstName());
                    response.setLastName(trainer.getUser().getLastName());
                    response.setUsername(trainer.getUser().getUsername());
                    response.setSpecialization(trainer.getSpecialization());
                    return response;
                }).toList());
        return dto;
    }

    public TrainerListResponseDto toTrainerListDto(Trainer trainer) {
        if (trainer == null) {
            return null;
        }
        TrainerListResponseDto dto = new TrainerListResponseDto();
        dto.setFirstName(trainer.getUser().getFirstName());
        dto.setLastName(trainer.getUser().getLastName());
        dto.setUsername(trainer.getUser().getUsername());
        dto.setSpecialization(trainer.getSpecialization());
        return dto;
    }
}
