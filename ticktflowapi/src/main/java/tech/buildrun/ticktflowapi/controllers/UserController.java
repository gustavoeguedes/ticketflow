package tech.buildrun.ticktflowapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Users", description = "Operações do usuário autenticado")
@RestController
public class UserController {

    @GetMapping("/me")
    @Operation(
            summary = "Obter dados do usuário autenticado",
            description = "Retorna os claims do JWT do usuário autenticado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Claims retornados"),
            @ApiResponse(responseCode = "401", description = "Authentication is required")
    })
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(jwt.getClaims());
    }
}
