package com.incubadora.incubadora.dev.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/mentor")
@Tag(name = "Panel de Mentor", description = "Endpoints específicos para Mentores")
public class MentorController {

    @Operation(
            summary = "Obtiene datos para el panel de Mentor",
            description = "Endpoint protegido que solo puede ser accedido por usuarios con el rol 'Mentor'.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de Mentor")
    @GetMapping
    @PreAuthorize("hasRole('Mentor')")
    public ResponseEntity<Map<String, String>> getMentorDashboard() {
        return ResponseEntity.ok(Map.of("message", "Bienvenido al panel de Mentor."));
    }
}

