package project.gamesync.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "GameSync API Documentation",
                version = "1.0",
                description = "Dokumentasi API untuk Project GameSync Backend",
                contact = @Contact(
                        name = "Jufrin",
                        email = "jufrinaha111@gmail.com"
                )
        ),
        // Menambahkan Security Requirement Global (Semua endpoint butuh token kecuali yang di-exclude)
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
    // Tidak perlu ada code di dalam body class
}