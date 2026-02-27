package tech.buildrun.ticktflowapi.controllers.dto;

import tech.buildrun.ticktflowapi.entities.Ticket;
import tech.buildrun.ticktflowapi.entities.TicketStatus;

import java.time.Instant;

public record TicketListDto(String id,
                            String title,
                            String description,
                            TicketStatus status,
                            Integer commentCount,
                            Instant createdAt) {
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
