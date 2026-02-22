package tech.buildrun.ticktflowapi.controllers.dto;

import tech.buildrun.ticktflowapi.entities.AuthorType;
import tech.buildrun.ticktflowapi.entities.Ticket;
import tech.buildrun.ticktflowapi.entities.TicketComment;

import java.util.UUID;

public record AddCommentDto(String message) {

    public TicketComment toEntity(Ticket ticket, AuthorType authorType, String authorId) {
        var entity = new TicketComment();
        entity.setMessage(message);
        entity.setTicketId(ticket);
        entity.setAuthorType(authorType);
        entity.setAuthorId(UUID.fromString(authorId));
        return entity;
    }
}
