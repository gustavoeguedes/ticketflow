package tech.buildrun.ticktflowapi.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketDto(
		@Schema(description = "Título curto do ticket", example = "Não consigo fazer login após redefinir a senha", minLength = 3, maxLength = 100)
		@NotBlank
		@Size(min = 3, max = 100)
		String title,

		@Schema(description = "Descrição detalhada do problema", example = "Troquei minha senha e agora o sistema retorna credenciais inválidas.", minLength = 3, maxLength = 255)
		@Size(min = 3, max = 255)
		String description
) {
}
