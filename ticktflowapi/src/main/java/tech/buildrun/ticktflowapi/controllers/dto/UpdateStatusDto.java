package tech.buildrun.ticktflowapi.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import tech.buildrun.ticktflowapi.entities.TicketStatus;

public record UpdateStatusDto(
		@Schema(
				description = "Novo status do ticket. Valores permitidos são os do enum TicketStatus.",
				example = "IN_PROGRESS"
		)
		@NotNull
		TicketStatus status
) {
}
