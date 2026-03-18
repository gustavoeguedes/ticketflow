package tech.buildrun.ticktflowapi.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.buildrun.ticktflowapi.entities.Ticket;

import java.util.List;

public record ReadTicketDto(
        @Schema(example = "db1e88ee-b3ef-4ba8-aef7-1f8e8a9ee6f6") String id,
        @Schema(example = "Não consigo fazer login após redefinir a senha") String title,
        @Schema(example = "Troquei minha senha e agora o sistema retorna credenciais inválidas.") String description,
        @Schema(example = "OPEN") String status,
        @Schema(example = "2026-03-18T10:15:30Z") String createdAt,
        @Schema(example = "2026-03-18T10:20:45Z") String updatedAt,
        List<ReadCommentDto> comments
) {

    public static ReadTicketDto fromEntity(Ticket ticket) {


        return new ReadTicketDto(
                ticket.getId().toString(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus().name(),
                ticket.getCreatedAt().toString(),
                ticket.getUpdatedAt().toString(),
                ReadCommentDto.fromEntities(ticket.getComments())
        );
    }
}
