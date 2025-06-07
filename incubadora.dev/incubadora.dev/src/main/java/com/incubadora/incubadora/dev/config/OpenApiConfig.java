package com.incubadora.incubadora.dev.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Incubadora.dev API",
                version = "v1.0.0",
                description = "API para la plataforma Incubadora.dev, gestionando desarrolladores, mentores, reclutadores y proyectos."
        ),
        // Aplica el requisito de seguridad a nivel global para todos los endpoints
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth", // Un nombre para referenciar este esquema. Debe coincidir con el de SecurityRequirement.
        description = "Token JWT para autorización. Ingresa **'Bearer' [espacio] y luego tu token.**",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP, // El tipo de esquema es HTTP.
        bearerFormat = "JWT", // El formato del token es JWT.
        in = SecuritySchemeIn.HEADER // El token se envía en la cabecera.
)
public class OpenApiConfig {
    // Esta clase puede estar vacía. Las anotaciones a nivel de clase hacen todo el trabajo.
}

