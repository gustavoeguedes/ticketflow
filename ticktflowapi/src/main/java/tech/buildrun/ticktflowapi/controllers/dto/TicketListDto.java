package tech.buildrun.ticktflowapi.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.buildrun.ticktflowapi.entities.Ticket;
import tech.buildrun.ticktflowapi.entities.TicketStatus;

import java.time.Instant;

public record TicketListDto(
        @Schema(example = "db1e88ee-b3ef-4ba8-aef7-1f8e8a9ee6f6") String id,
        @Schema(example = "Não consigo fazer login após redefinir a senha") String title,
        @Schema(example = "Troquei minha senha e agora o sistema retorna credenciais inválidas.") String description,
        @Schema(example = "OPEN") TicketStatus status,
        @Schema(example = "2") Integer commentCount,
        @Schema(example = "2026-03-18T10:15:30Z") Instant createdAt
) {
    public static TicketListDto fromEntity(Ticket ticket) {
        return new TicketListDto(
                ticket.getId().toString(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getComments() != null ? ticket.getComments().size() : 0,
                ticket.getCreatedAt()
        );
    }
}
