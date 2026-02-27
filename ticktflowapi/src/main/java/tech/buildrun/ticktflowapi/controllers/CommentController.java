package tech.buildrun.ticktflowapi.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.buildrun.ticktflowapi.controllers.dto.AddCommentDto;
import tech.buildrun.ticktflowapi.service.CommentService;

import java.util.UUID;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(value = "/tickets/{ticketId}/comments")
    @PreAuthorize("hasAnyAuthority('tickets-comments:create', 'own:tickets-comments:create')")
    public ResponseEntity<Void> addCommentToTicket(@AuthenticationPrincipal Jwt jwt,
                                                   @PathVariable UUID ticketId,
                                                   @RequestBody @Valid AddCommentDto dto) {

        var commentAddedWithSuccess = commentService.addCommentToTicket(ticketId, dto, jwt);

        if(!commentAddedWithSuccess) {
            return ResponseEntity.unprocessableContent().build();
        }

        return ResponseEntity.ok().build();
    }
}
