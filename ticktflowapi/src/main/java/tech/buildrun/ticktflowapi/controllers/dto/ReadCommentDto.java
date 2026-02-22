package tech.buildrun.ticktflowapi.controllers.dto;

import tech.buildrun.ticktflowapi.entities.TicketComment;

import java.util.List;

public record ReadCommentDto(String id,
                             String authorId,
                             String authorType,
                             String message,
                             String createdAt) {
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
