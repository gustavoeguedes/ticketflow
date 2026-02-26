package tech.buildrun.ticktflowapi.controllers;

import jakarta.validation.Valid;
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
import tech.buildrun.ticktflowapi.service.CommentService;

import java.util.UUID;

@RestController
public class CommentController {

    private final TicketRepository ticketRepository;
    private final TicketCommentRepository ticketCommentRepository;
    private final CommentService commentService;

    public CommentController(TicketRepository ticketRepository,
                             TicketCommentRepository ticketCommentRepository,
                             CommentService commentService) {
        this.ticketRepository = ticketRepository;
        this.ticketCommentRepository = ticketCommentRepository;
        this.commentService = commentService;
    }

    @PostMapping(value = "/tickets/{ticketId}/comments")
    @PreAuthorize("hasAnyAuthority('tickets-comments:create', 'own:tickets-comments:create')")
    public ResponseEntity<Void> addCommentToTicket(@AuthenticationPrincipal Jwt jwt,
                                                   @PathVariable UUID ticketId,
                                                   @RequestBody @Valid AddCommentDto dto) {

        var commentAddedWithSuccess = commentService.addCommentToTicket(ticketId, dto, jwt);

        return ResponseEntity.ok().build();
    }
}
