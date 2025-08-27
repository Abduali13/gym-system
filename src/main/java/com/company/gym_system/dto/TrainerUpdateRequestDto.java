package com.company.gym_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainerUpdateRequestDto {

    @Schema(description = "First name of the trainer", example = "Alice", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String firstName;

    @Schema(description = "Last name of the trainer", example = "Smith", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String lastName;
    private String specialization;

    @Schema(description = "Indicates whether the trainer is active", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private Boolean isActive;

    @Schema(description = "Username of the trainer", example = "alice_smith", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String username;

    @Schema(description = "Password of the trainer", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String password;
}
