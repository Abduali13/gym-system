package com.company.gym_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TraineeUpdateRequestDto {

    @Schema(description = "First name of the trainee", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String firstName;

    @Schema(description = "Last name of the trainee", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String lastName;

    @Schema(description = "Username of the trainee", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String username;

    @Schema(description = "Password of the trainee", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String password;

    private LocalDate birthDate;
    private String address;

    @Schema(description = "Indicates whether the trainee is active", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private Boolean isActive;

}
