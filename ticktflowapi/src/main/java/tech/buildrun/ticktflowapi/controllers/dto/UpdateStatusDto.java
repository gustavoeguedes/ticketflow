package tech.buildrun.ticktflowapi.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.buildrun.ticktflowapi.entities.TicketStatus;


public record UpdateStatusDto(@NotBlank TicketStatus status) {
}
