package tech.buildrun.ticktflowapi.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.ticktflowapi.controllers.dto.*;
import tech.buildrun.ticktflowapi.service.TicketService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {


    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('tickets:create')")
    public ResponseEntity<Void> createTicket(@AuthenticationPrincipal Jwt jwt,
                                             @RequestBody @Valid CreateTicketDto dto
                                             ) {

        var ownerId = jwt.getSubject();

        var ticketEntity = ticketService.createTicket(dto, ownerId);

        return ResponseEntity.created(URI.create("/tickets/" + ticketEntity.getId() )).build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('tickets:list', 'own:tickets:list')")
    public ResponseEntity<ApiResponse<TicketListDto>> listTickets(@AuthenticationPrincipal Jwt jwt,
                                                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var ticketsPage = ticketService.getTickets(jwt, page, pageSize);

        var resp = ApiResponse.of(ticketsPage);

        return ResponseEntity.ok(resp);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('tickets-status:update')")
    public ResponseEntity<Void> updateTicketStatus(@PathVariable String id,
                                                   @RequestBody UpdateStatusDto dto) {

        var statusUpdatedWithSuccess = ticketService.updateTicketStatus(UUID.fromString(id), dto);

        if (statusUpdatedWithSuccess) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.unprocessableContent().build();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('tickets:read', 'own:tickets:read')")
    public ResponseEntity<ReadTicketDto> getTicket(@PathVariable UUID id,
                                                   @AuthenticationPrincipal Jwt jwt) {
        var ticket = ticketService.getTicketById(id, jwt);

        return ResponseEntity.ok(ReadTicketDto.fromEntity(ticket));

    }


}
