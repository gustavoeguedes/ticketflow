package tech.buildrun.ticktflowapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.ticktflowapi.entities.Ticket;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByOwnerId(UUID userId);
}
