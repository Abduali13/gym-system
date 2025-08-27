package com.company.gym_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TrainerRegistrationRequestDto {

    @Schema(description = "First name of the trainer", example = "Alice", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String firstName;

    @Schema(description = "Last name of the trainer", example = "Smith", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String lastName;

    @Schema(description = "Username of the trainer", example = "alice_smith", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String specialization;

}
