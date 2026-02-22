package tech.buildrun.ticktflowapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.ticktflowapi.controllers.dto.CreateTicketDto;
import tech.buildrun.ticktflowapi.controllers.dto.ReadTicketDto;
import tech.buildrun.ticktflowapi.controllers.dto.TicketListDto;
import tech.buildrun.ticktflowapi.controllers.dto.UpdateStatusDto;
import tech.buildrun.ticktflowapi.entities.Ticket;
import tech.buildrun.ticktflowapi.entities.TicketStatus;
import tech.buildrun.ticktflowapi.repository.TicketRepository;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('tickets:create')")
    public ResponseEntity<Void> createTicket(@AuthenticationPrincipal Jwt jwt,
                                             @RequestBody CreateTicketDto dto
                                             ) {

        var ticket = new Ticket();
        ticket.setTitle(dto.title());
        ticket.setDescription(dto.description());
        ticket.setOwnerId(UUID.fromString(jwt.getSubject()));
        ticket.setStatus(TicketStatus.OPEN);
        var ticketEntity = ticketRepository.save(ticket);


        return ResponseEntity.created(URI.create("/tickets/" + ticketEntity.getId() )).build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('tickets:list', 'own:tickets:list')")
    public ResponseEntity<List<TicketListDto>> listTickets(@AuthenticationPrincipal Jwt jwt) {
        List<TicketListDto> tickets;

        if (jwt.getClaimAsStringList("scp").contains("tickets:list")) {

            tickets = ticketRepository.findAll().stream()
                    .map(TicketListDto::fromEntity)
                    .toList();
        } else if (jwt.getClaimAsStringList("scp").contains("own:tickets:list")) {

            var userId = UUID.fromString(jwt.getSubject());
            tickets = ticketRepository.findByOwnerId(userId)
                    .stream()
                    .map(TicketListDto::fromEntity)
                    .toList();
        } else {
            System.out.println("User " + jwt.getSubject() + " does not have permission to list tickets");
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(tickets);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('tickets-status:update')")
    public ResponseEntity<Void> updateTicketStatus(@PathVariable String id,
                                                   @RequestBody UpdateStatusDto dto) {
        var ticketOpt = ticketRepository.findById(UUID.fromString(id));
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var ticket = ticketOpt.get();

        if (canMoveToInProgress(ticket, dto.status()) || canMarkAsSolvedOrReject(ticket, dto.status())) {
            ticket.setStatus(dto.status());
            ticketRepository.save(ticket);
        } else {
            return ResponseEntity.unprocessableContent().build();
        }

        return ResponseEntity.noContent().build();
    }

    private boolean canMoveToInProgress(Ticket ticket, TicketStatus status) {
        return ticket.getStatus().equals(TicketStatus.OPEN) && status.equals(TicketStatus.IN_PROGRESS);
    }

    private boolean canMarkAsSolvedOrReject(Ticket ticket, TicketStatus status) {
        return ticket.getStatus().equals(TicketStatus.IN_PROGRESS) && (status.equals(TicketStatus.SOLVED) || status.equals(TicketStatus.REJECTED));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('tickets:read', 'own:tickets:read')")
    public ResponseEntity<ReadTicketDto> getTicket(@PathVariable UUID id,
                                                   @AuthenticationPrincipal Jwt jwt) {
        var ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        if (isAllowedToReadAllTickets(jwt) || isAllowedToReadOwnTickets(jwt, ticket)) {
            return ResponseEntity.ok(ReadTicketDto.fromEntity(ticket));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    private boolean isAllowedToReadOwnTickets(Jwt jwt, Ticket ticket) {
        return jwt.getClaimAsStringList("scp").contains("own:tickets:read") &&
                ticket.getOwnerId().equals(UUID.fromString(jwt.getSubject()));
    }

    private boolean isAllowedToReadAllTickets(Jwt jwt) {
        return jwt.getClaimAsStringList("scp").contains("tickets:read");
    }


}
