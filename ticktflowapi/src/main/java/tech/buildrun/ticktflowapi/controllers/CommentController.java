package tech.buildrun.ticktflowapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Comments", description = "Operações de comentários em tickets")
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(value = "/tickets/{ticketId}/comments")
    @PreAuthorize("hasAnyAuthority('tickets-comments:create', 'own:tickets-comments:create')")
    @Operation(
            summary = "Adicionar comentário ao ticket",
            description = "Adiciona um comentário no ticket. O ticket precisa estar com status IN_PROGRESS.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentário adicionado"),
            @ApiResponse(responseCode = "400", description = "There is invalid fields on the request"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "The requested ticket does not exist"),
            @ApiResponse(responseCode = "422", description = "Comments can only be added when the ticket is IN_PROGRESS")
    })
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
