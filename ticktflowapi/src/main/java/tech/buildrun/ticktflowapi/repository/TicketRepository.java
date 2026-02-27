package tech.buildrun.ticktflowapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.ticktflowapi.entities.Ticket;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findByOwnerId(UUID userId, Pageable pageRequest);
}
