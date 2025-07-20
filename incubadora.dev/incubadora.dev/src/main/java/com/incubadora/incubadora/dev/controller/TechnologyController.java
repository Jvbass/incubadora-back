package com.incubadora.incubadora.dev.controller;


import com.incubadora.incubadora.dev.service.TechnologyService;
import com.incubadora.incubadora.dev.dto.TechnologyDto;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
@Tag(name = "Tecnologías", description = "Operaciones relacionadas con las tecnologías utilizadas en los proyectos")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @Operation(
            summary = "Obtiene una lista de todas las tecnologías disponibles",
            description = "Accesible por cualquier usuario autenticado. Útil para rellenar formularios en el frontend, como la creación de proyectos.",
            security = @SecurityRequirement(name = "bearerAuth")
    )


    @ApiResponse(responseCode = "200", description = "Lista de tecnologías obtenida exitosamente")
    @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
    @GetMapping
    @PreAuthorize("isAuthenticated()") // Permite el acceso a CUALQUIER usuario autenticado
    public ResponseEntity<List<TechnologyDto>> getAllTechnologies() {
        List<TechnologyDto> technologies = technologyService.getAllTechnologies();
        return ResponseEntity.ok(technologies);
    }
}
