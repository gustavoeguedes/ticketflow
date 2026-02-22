package tech.buildrun.ticktflowapi.controllers.dto;

import tech.buildrun.ticktflowapi.entities.Ticket;

import java.util.List;

public record ReadTicketDto(String id,
                            String title,
                            String description,
                            String status,
                            String createdAt,
                            String updatedAt,
                            List<ReadCommentDto> comments) {

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
