package tech.buildrun.ticktflowapi.controllers.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketDto(@NotBlank @Size(min = 3, max = 100) String title, @Size(min = 3, max = 255) String description) {
}
