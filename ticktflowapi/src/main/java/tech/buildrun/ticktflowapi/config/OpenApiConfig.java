package tech.buildrun.ticktflowapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI ticketFlowOpenApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("TicketFlow API")
                        .version("v1")
                        .description("""
                                API de gerenciamento de tickets.

                                Autenticação no Swagger:
                                1. Clique em Authorize (ícone de cadeado).
                                2. Informe: Bearer <seu_token_jwt>.

                                Rota de token do Keycloak (ambiente local):
                                POST http://localhost:8080/realms/ticketflow-realm/protocol/openid-connect/token
                                """)
                        .license(new License().name("Uso interno")))
                        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                        .tags(java.util.List.of(
                                new Tag().name("Tickets").description("Criação, consulta e atualização de tickets"),
                                new Tag().name("Comments").description("Operações de comentários dos tickets"),
                                new Tag().name("Users").description("Dados do usuário autenticado")
                        ));
    }


}