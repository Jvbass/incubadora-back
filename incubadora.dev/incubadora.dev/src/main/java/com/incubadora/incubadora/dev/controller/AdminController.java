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
@RequestMapping("/api/admin")
@Tag(name = "Panel de Administrador", description = "Endpoints específicos para administradores")
public class AdminController {

    @Operation(
            summary = "Obtiene datos para el panel de administrador",
            description = "Endpoint protegido que solo puede ser accedido por usuarios con el rol 'Administrador'.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de Administrador")
    @GetMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Map<String, String>> getAdminDashboard() {
        return ResponseEntity.ok(Map.of("message", "Bienvenido al panel de Administrador."));
    }
}

