package tech.buildrun.ticktflowapi.controllers.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateTicketDto(@NotBlank @Min(3) @Max(100) String title, @Min(3) @Max(255)  String description) {
}
