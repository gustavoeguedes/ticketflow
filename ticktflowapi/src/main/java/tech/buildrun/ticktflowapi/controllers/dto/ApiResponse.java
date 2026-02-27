package tech.buildrun.ticktflowapi.controllers.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record ApiResponse<T>(List<T> data, PaginationResponse pagination) {

    public static <T> ApiResponse<T> of(Page<T> page) {
        return new ApiResponse<T>(page.getContent(),
                new PaginationResponse(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()));
    }
}
