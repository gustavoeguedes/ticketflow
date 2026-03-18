package tech.buildrun.ticktflowapi.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalhes da paginação")
public record PaginationResponse(
        @Schema(description = "Índice da página atual (base 0)", example = "0")
        Integer page,

        @Schema(description = "Quantidade de itens por página", example = "10")
        Integer pageSize,

        @Schema(description = "Quantidade total de itens", example = "57")
        Long totalElements,

        @Schema(description = "Quantidade total de páginas", example = "6")
        Integer totalPages
) {

}
