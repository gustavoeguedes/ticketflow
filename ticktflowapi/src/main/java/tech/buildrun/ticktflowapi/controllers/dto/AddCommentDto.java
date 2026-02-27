package tech.buildrun.ticktflowapi.controllers.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tech.buildrun.ticktflowapi.entities.AuthorType;
import tech.buildrun.ticktflowapi.entities.Ticket;
import tech.buildrun.ticktflowapi.entities.TicketComment;

import java.util.UUID;

public record AddCommentDto(@NotBlank @Size(min = 3, max = 100) String message) {

    public TicketComment toEntity(Ticket ticket, AuthorType authorType, String authorId) {
        var entity = new TicketComment();
        entity.setMessage(message);
        entity.setTicketId(ticket);
        entity.setAuthorType(authorType);
        entity.setAuthorId(UUID.fromString(authorId));
        return entity;
    }
}
