package com.incubadora.incubadora.dev.controller;


import com.incubadora.incubadora.dev.dto.DashboardResponseDto;
import com.incubadora.incubadora.dev.entity.core.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "2. Dashboard", description = "Endpoints para el panel de usuario")
public class DashboardController {

    @Operation(
            summary = "Obtiene la información del usuario actualmente autenticado",
            description = "Endpoint protegido que devuelve los detalles del usuario logueado. Requiere un token JWT válido.",
            security = @SecurityRequirement(name = "bearerAuth") // Indica que este endpoint requiere el token
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información del usuario obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DashboardResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token no válido o ausente")
    })
    @GetMapping
    @PreAuthorize("hasRole('Desarrollador')") // Solo permite acceso a usuarios con el rol "Desarrollador"
    public ResponseEntity<DashboardResponseDto> getDashboardInfo() {
        // 1. Obtener el objeto Authentication del contexto de seguridad de Spring
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // El 'Principal' es el objeto UserDetails que cargamos en el JwtAuthFilter.
        // Lo casteamos a nuestra entidad User, ya que User implementa UserDetails.
        User currentUser = (User) authentication.getPrincipal();

        // 2. Mapear los datos de la entidad User al DTO de respuesta
        DashboardResponseDto responseDto = new DashboardResponseDto(
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getRole().getName()
        );

        // 3. Devolver el DTO en la respuesta
        return ResponseEntity.ok(responseDto);
    }


}

