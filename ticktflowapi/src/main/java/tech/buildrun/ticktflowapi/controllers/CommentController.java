package tech.buildrun.ticktflowapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.ticktflowapi.controllers.dto.AddCommentDto;
import tech.buildrun.ticktflowapi.entities.AuthorType;
import tech.buildrun.ticktflowapi.entities.TicketComment;
import tech.buildrun.ticktflowapi.entities.TicketStatus;
import tech.buildrun.ticktflowapi.repository.TicketCommentRepository;
import tech.buildrun.ticktflowapi.repository.TicketRepository;

import java.util.UUID;

@RestController
public class CommentController {

    private final TicketRepository ticketRepository;
    private final TicketCommentRepository ticketCommentRepository;

    public CommentController(TicketRepository ticketRepository, TicketCommentRepository ticketCommentRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketCommentRepository = ticketCommentRepository;
    }

    @PostMapping(value = "/tickets/{ticketId}/comments")
    @PreAuthorize("hasAnyAuthority('tickets-comments:create', 'own:tickets-comments:create')")
    public ResponseEntity<Void> addCommentToTicket(@AuthenticationPrincipal Jwt jwt,
                                                   @PathVariable UUID ticketId,
                                                   @RequestBody AddCommentDto dto) {


        var ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );





        if (jwt.getClaimAsStringList("scp").contains("own:tickets-comments:create") &&
                !ticket.getOwnerId().equals(UUID.fromString(jwt.getSubject()))) {
            return ResponseEntity.status(403).build();
        }

        if(ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            return ResponseEntity.unprocessableContent().build();
        }

        TicketComment ticketComment;

        if(jwt.getClaimAsStringList("scp").contains("tickets-comments:create")) {
            ticketComment = dto.toEntity(ticket, AuthorType.SUPPORT, jwt.getSubject());
        } else {
            ticketComment = dto.toEntity(ticket, AuthorType.USER, jwt.getSubject());
        }
        ticketCommentRepository.save(ticketComment);


        return ResponseEntity.ok().build();
    }
}
