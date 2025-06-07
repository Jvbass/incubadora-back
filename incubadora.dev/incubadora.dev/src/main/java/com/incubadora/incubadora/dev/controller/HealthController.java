package com.incubadora.incubadora.dev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api") // Define un prefijo base para todos los endpoints en este controlador
@Tag(name = "0.Health_Check", description = "Endpoints para verificar el estado del servicio") // Para agrupar en Swagger
public class HealthController {

    @Operation(
            summary = "Verifica el estado de la aplicación",
            description = "Devuelve un mensaje simple indicando que el sistema está operativo. " +
                    "Este endpoint es público y no requiere autenticación.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sistema operativo",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "object", example = "{\"status\": \"Sistema Incubadora.dev está corriendo correctamente!\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Servicio no disponible (esto sería para un health check más avanzado)"
                    )
            }
    )
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> checkHealth() {
        // Crea un mapa simple para la respuesta JSON
        Map<String, String> response = Collections.singletonMap("status", "Sistema Incubadora.dev está corriendo correctamente!");
        return ResponseEntity.ok(response);
    }
}

