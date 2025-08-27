package com.company.gym_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TraineeRegistrationRequestDto {
    @Schema(description = "First name of the trainee", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String firstName;

    @Schema(description = "Last name of the trainee", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String lastName;

    @Schema(description = "Date of birth of the trainee", example = "1990-05-15")
    private LocalDate birthDate;

    @Schema(description = "Address of the trainee", example = "123 Main St, Springfield")
    private String address;
}