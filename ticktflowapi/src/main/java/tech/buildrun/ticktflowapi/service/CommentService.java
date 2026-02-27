package tech.buildrun.ticktflowapi.service;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.ticktflowapi.controllers.dto.AddCommentDto;
import tech.buildrun.ticktflowapi.entities.AuthorType;
import tech.buildrun.ticktflowapi.entities.Ticket;
import tech.buildrun.ticktflowapi.entities.TicketComment;
import tech.buildrun.ticktflowapi.entities.TicketStatus;
import tech.buildrun.ticktflowapi.exception.TicketNotFoundException;
import tech.buildrun.ticktflowapi.repository.TicketCommentRepository;
import tech.buildrun.ticktflowapi.repository.TicketRepository;

import java.util.UUID;

@Service
public class CommentService {

    private final TicketRepository ticketRepository;
    private final TicketCommentRepository ticketCommentRepository;

    public CommentService(TicketRepository ticketRepository, TicketCommentRepository ticketCommentRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketCommentRepository = ticketCommentRepository;
    }

    public boolean addCommentToTicket(UUID ticketId, AddCommentDto dto, Jwt jwt) {
        boolean addCommentToTicketWithSuccess = false;
        var ticket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);

        if (jwt.getClaimAsStringList("scp").contains("own:tickets-comments:create") &&
                !ticket.getOwnerId().equals(UUID.fromString(jwt.getSubject()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if(ticket.getStatus() == TicketStatus.IN_PROGRESS) {
            addCommentToTicketWithSuccess = true;
            var ticketComment = getTicketComment(dto, jwt, ticket);
            ticketCommentRepository.save(ticketComment);
        }


        return addCommentToTicketWithSuccess;
    }

    private TicketComment getTicketComment(AddCommentDto dto, Jwt jwt, Ticket ticket) {
        TicketComment ticketComment;
        if(jwt.getClaimAsStringList("scp").contains("tickets-comments:create")) {
            ticketComment = dto.toEntity(ticket, AuthorType.SUPPORT, jwt.getSubject());
        } else {
            ticketComment = dto.toEntity(ticket, AuthorType.USER, jwt.getSubject());
        }
        return ticketComment;
    }

}
