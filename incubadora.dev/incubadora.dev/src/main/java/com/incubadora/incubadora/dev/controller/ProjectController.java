package com.incubadora.incubadora.dev.controller;


import com.incubadora.incubadora.dev.dto.CreateProjectRequestDto;
import com.incubadora.incubadora.dev.dto.ProjectResponseDto;
import com.incubadora.incubadora.dev.dto.ProjectSummaryDto;
import com.incubadora.incubadora.dev.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "4. Proyectos", description = "Operaciones relacionadas con los proyectos de los usuarios")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(
            summary = "Crea un nuevo proyecto",
            description = "Permite a un usuario autenticado con el rol 'Desarrollador' publicar un nuevo proyecto.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "201", description = "Proyecto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "403", description = "Acceso denegado, se requiere rol 'Desarrollador'")
    @PostMapping
    @PreAuthorize("hasRole('Desarrollador')")
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody CreateProjectRequestDto request, Authentication authentication) {
        // Obtenemos el nombre de usuario del principal autenticado
        String username = authentication.getName();

        ProjectResponseDto createdProject = projectService.createProject(request, username);

        // Devolvemos un código 201 Created junto con el proyecto creado
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    //  obtener una lista de todos los proyectos publicados ---/api/projects
    @Operation(
            summary = "Obtiene una lista de todos los proyectos publicados",
            description = "Accesible por cualquier usuario autenticado, sin importar su rol. Devuelve una lista de resúmenes.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    @PreAuthorize("isAuthenticated()") // <-- Permite el acceso a cualquier usuario logueado
    public ResponseEntity<List<ProjectSummaryDto>> getAllProjects() {
        List<ProjectSummaryDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    //  obtener el detalle de un proyecto ---/api/projects/{id}
    @Operation(
            summary = "Obtiene los detalles completos de un proyecto por su ID",
            description = "Accesible por cualquier usuario autenticado. Ideal para la vista de detalle del proyecto.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Integer id) {
        ProjectResponseDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }
}

