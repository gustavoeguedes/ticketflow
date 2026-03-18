package tech.buildrun.ticktflowapi.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.buildrun.ticktflowapi.entities.TicketComment;

import java.util.List;

public record ReadCommentDto(
        @Schema(example = "5be85ef8-9fa8-490d-9e2e-7c369bf53dc8") String id,
        @Schema(example = "0d3f6cbe-6f84-4f2d-a4f2-a9d5de8c9d5f") String authorId,
        @Schema(example = "SUPPORT") String authorType,
        @Schema(example = "Estamos analisando seu caso e atualizaremos em até 24h.") String message,
        @Schema(example = "2026-03-18T10:30:00Z") String createdAt
) {
    public static List<ReadCommentDto> fromEntities(List<TicketComment> comments) {
        return comments.stream()
                .map(comment -> new ReadCommentDto(
                        comment.getId().toString(),
                        comment.getAuthorId().toString(),
                        comment.getAuthorType().name(),
                        comment.getMessage(),
                        comment.getCreatedAt().toString()
                ))
                .toList();
    }
}
