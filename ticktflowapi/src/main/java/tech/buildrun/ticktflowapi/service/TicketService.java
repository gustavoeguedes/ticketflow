package tech.buildrun.ticktflowapi.service;

import org.springframework.http.HttpStatus;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.ticktflowapi.controllers.dto.CreateTicketDto;
import tech.buildrun.ticktflowapi.controllers.dto.TicketListDto;
import tech.buildrun.ticktflowapi.controllers.dto.UpdateStatusDto;
import tech.buildrun.ticktflowapi.entities.Ticket;
import tech.buildrun.ticktflowapi.entities.TicketStatus;
import tech.buildrun.ticktflowapi.exception.TicketNotFoundException;
import tech.buildrun.ticktflowapi.repository.TicketRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(CreateTicketDto dto, String ownerId) {
        var ticket = new Ticket();
        ticket.setTitle(dto.title());
        ticket.setDescription(dto.description());
        ticket.setOwnerId(UUID.fromString(ownerId));
        ticket.setStatus(TicketStatus.OPEN);
        return ticketRepository.save(ticket);
    }

    public List<TicketListDto> getTickets(Jwt jwt) {
        List<TicketListDto> ticketList;
        if (jwt.getClaimAsStringList("scp").contains("tickets:list")) {

            ticketList = this.getAllTickets();

        } else if (jwt.getClaimAsStringList("scp").contains("own:tickets:list")) {

            var userId = UUID.fromString(jwt.getSubject());
            ticketList = this.getTicketsByOwnerId(userId);

            } else {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this resource");

        }

        return ticketList;

    }

    private List<TicketListDto> getAllTickets() {
        return ticketRepository.findAll().stream()
            .map(TicketListDto::fromEntity)
            .toList();
    }

    private List<TicketListDto> getTicketsByOwnerId(UUID ownerId) {
        return ticketRepository.findByOwnerId(ownerId).stream()
            .map(TicketListDto::fromEntity)
            .toList();
    }

    public boolean updateTicketStatus(UUID id, UpdateStatusDto dto) {
        var ticket = ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);



        if (canMoveToInProgress(ticket, dto.status()) || canMarkAsSolvedOrReject(ticket, dto.status())) {
            ticket.setStatus(dto.status());
            ticketRepository.save(ticket);
                return true;
        }
        return false;

    }

    public Ticket getTicketById(UUID id, Jwt jwt) {
        var ticket = ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);

        if(!isAllowedToReadAllTickets(jwt) || !isAllowedToReadOwnTickets(jwt, ticket)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return ticket;
    }

    private boolean canMoveToInProgress(Ticket ticket, TicketStatus status) {
        return ticket.getStatus().equals(TicketStatus.OPEN) && status.equals(TicketStatus.IN_PROGRESS);
    }

    private boolean canMarkAsSolvedOrReject(Ticket ticket, TicketStatus status) {
        return ticket.getStatus().equals(TicketStatus.IN_PROGRESS) && (status.equals(TicketStatus.SOLVED) || status.equals(TicketStatus.REJECTED));
    }

    private boolean isAllowedToReadOwnTickets(Jwt jwt, Ticket ticket) {
        return jwt.getClaimAsStringList("scp").contains("own:tickets:read") &&
                ticket.getOwnerId().equals(UUID.fromString(jwt.getSubject()));
    }

    private boolean isAllowedToReadAllTickets(Jwt jwt) {
        return jwt.getClaimAsStringList("scp").contains("tickets:read");
    }
}
