package tech.buildrun.ticktflowapi.controllers.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Envelope padrão de resposta paginada")
public record ApiResponse<T>(
        @ArraySchema(arraySchema = @Schema(description = "Itens da página atual"))
        List<T> data,

        @Schema(description = "Metadados de paginação")
        PaginationResponse pagination
) {

    public static <T> ApiResponse<T> of(Page<T> page) {
        return new ApiResponse<T>(page.getContent(),
                new PaginationResponse(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()));
    }
}
