package tech.buildrun.ticktflowapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.buildrun.ticktflowapi.controllers.dto.CreateTicketDto;
import tech.buildrun.ticktflowapi.controllers.dto.ReadTicketDto;
import tech.buildrun.ticktflowapi.controllers.dto.TicketListDto;
import tech.buildrun.ticktflowapi.controllers.dto.UpdateStatusDto;
import tech.buildrun.ticktflowapi.service.TicketService;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Tickets", description = "Operações para gerenciamento de tickets")
@RestController
@RequestMapping("/tickets")
public class TicketController {


    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('tickets:create')")
    @Operation(
            summary = "Criar ticket",
            description = "Cria um novo ticket para o usuário autenticado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ticket criado"),
            @ApiResponse(responseCode = "400", description = "There is invalid fields on the request"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "500", description = "Contact TicketFlow support")
    })
    public ResponseEntity<Void> createTicket(@AuthenticationPrincipal Jwt jwt,
                                             @RequestBody @Valid CreateTicketDto dto
                                             ) {

        var ownerId = jwt.getSubject();

        var ticketEntity = ticketService.createTicket(dto, ownerId);

        return ResponseEntity.created(URI.create("/tickets/" + ticketEntity.getId() )).build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('tickets:list', 'own:tickets:list')")
    @Operation(
            summary = "Listar tickets",
            description = "Retorna tickets paginados. Com 'tickets:list' lista todos; com 'own:tickets:list' lista apenas os do próprio usuário.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tickets retornados com paginação"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource")
    })
    public ResponseEntity<tech.buildrun.ticktflowapi.controllers.dto.ApiResponse<TicketListDto>> listTickets(@AuthenticationPrincipal Jwt jwt,
                                                                  @Parameter(description = "Índice da página (começa em 0)", example = "0")
                                                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                  @Parameter(description = "Quantidade de itens por página", example = "10")
                                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var ticketsPage = ticketService.getTickets(jwt, page, pageSize);

        var resp = tech.buildrun.ticktflowapi.controllers.dto.ApiResponse.of(ticketsPage);

        return ResponseEntity.ok(resp);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('tickets-status:update')")
    @Operation(
            summary = "Atualizar status do ticket",
            description = "Atualiza o status respeitando as transições de negócio: OPEN -> IN_PROGRESS e IN_PROGRESS -> SOLVED ou REJECTED.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status atualizado"),
            @ApiResponse(responseCode = "400", description = "There is invalid fields on the request"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "The requested ticket does not exist"),
            @ApiResponse(responseCode = "422", description = "Status transition is not allowed for the current ticket state")
    })
    public ResponseEntity<Void> updateTicketStatus(@PathVariable UUID id,
                                                   @RequestBody @Valid UpdateStatusDto dto) {

        var statusUpdatedWithSuccess = ticketService.updateTicketStatus(id, dto);

        if (statusUpdatedWithSuccess) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.unprocessableContent().build();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('tickets:read', 'own:tickets:read')")
    @Operation(
            summary = "Buscar ticket por id",
            description = "Retorna os detalhes do ticket e seus comentários quando o usuário tem permissão de acesso.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket retornado"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "The requested ticket does not exist")
    })
    public ResponseEntity<ReadTicketDto> getTicket(@PathVariable UUID id,
                                                   @AuthenticationPrincipal Jwt jwt) {
        var ticket = ticketService.getTicketById(id, jwt);

        return ResponseEntity.ok(ReadTicketDto.fromEntity(ticket));

    }


}
