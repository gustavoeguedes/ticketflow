package tech.buildrun.ticktflowapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.ticktflowapi.entities.TicketComment;

import java.util.UUID;

public interface TicketCommentRepository extends JpaRepository<TicketComment, UUID> {
}
