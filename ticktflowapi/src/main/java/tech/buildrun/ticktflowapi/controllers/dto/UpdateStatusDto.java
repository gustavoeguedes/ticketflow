package tech.buildrun.ticktflowapi.controllers.dto;

import tech.buildrun.ticktflowapi.entities.TicketStatus;

public record UpdateStatusDto(TicketStatus status) {
}
