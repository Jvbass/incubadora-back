package com.incubadora.incubadora.dev.controller;

import com.incubadora.incubadora.dev.dto.CreateProjectRequestDto;
import com.incubadora.incubadora.dev.dto.ProjectResponseDto;
import com.incubadora.incubadora.dev.dto.ProjectSummaryDto;
import com.incubadora.incubadora.dev.entity.core.User;
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

/**
 * Controlador REST para gestionar las operaciones relacionadas con los proyectos.
 * Expone endpoints para crear, listar y obtener detalles de proyectos.
 * Todas las operaciones requieren que el usuario esté autenticado.
 */
@RestController
@RequestMapping("/api/projects") // Define la ruta base para todos los endpoints de este controlador.
@Tag(name = "Proyectos", description = "Operaciones relacionadas con los proyectos de los usuarios")
public class ProjectController {

    // Inyección de dependencia del servicio que contiene la lógica de negocio para los proyectos.
    private final ProjectService projectService;

    /**
     * Constructor para la inyección de dependencias de Spring.
     *
     * @param projectService El servicio que se encargará de la lógica de los proyectos.
     */
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Endpoint para crear un nuevo proyecto.
     * Solo los usuarios con el rol 'Desarrollador' pueden acceder a este endpoint.
     *
     * @param request        El cuerpo de la petición con los datos para crear el proyecto. Se valida automáticamente.
     * @param authentication Objeto inyectado por Spring Security que contiene la información del usuario autenticado.
     * @return Una respuesta HTTP 201 (Created) con los datos del proyecto recién creado.
     */
    @Operation(
            summary = "Crea un nuevo proyecto",
            description = "Permite a un usuario autenticado con el rol 'Desarrollador' publicar un nuevo proyecto.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "201", description = "Proyecto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "403", description = "Acceso denegado, se requiere rol 'Desarrollador'")
    @PostMapping
    // @PreAuthorize: Anotación de Spring Security para control de acceso a nivel de método.
    // 'hasRole("Desarrollador")' significa que solo usuarios con ese rol específico pueden ejecutar este método.
    @PreAuthorize("hasRole('Desarrollador')")
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody CreateProjectRequestDto request, Authentication authentication) {
        // Obtenemos el nombre de usuario (username) del objeto Authentication.
        // Este objeto representa al principal (usuario) que ha sido autenticado por el JwtAuthFilter.
        String username = authentication.getName();

        // Se delega la lógica de creación al servicio, pasando los datos del DTO y el nombre del usuario.
        ProjectResponseDto createdProject = projectService.createProject(request, username);

        // Se devuelve una respuesta con el código de estado HTTP 201 Created, que es el estándar para
        // la creación exitosa de un recurso. El cuerpo de la respuesta contiene el proyecto creado.
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }


    /**
     * Endpoint para obtener una lista resumida de todos los proyectos con estado published.
     * Accesible por cualquier usuario que esté autenticado, sin importar su rol.
     *
     * @return Una respuesta HTTP 200 (OK) con una lista de resúmenes de proyectos.
     */
    @Operation(
            summary = "Obtiene una lista de todos los proyectos publicados",
            description = "Accesible por cualquier usuario autenticado, sin importar su rol. Devuelve una lista de resúmenes.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    // 'isAuthenticated()' permite el acceso a cualquier usuario que haya iniciado sesión correctamente.
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProjectSummaryDto>> getAllProjects() {
        List<ProjectSummaryDto> projects = projectService.getAllPublishedProjects();
        // ResponseEntity.ok() es un atajo para crear una respuesta con estado 200 OK.
        return ResponseEntity.ok(projects);
    }


//    /**
//     * Endpoint para obtener los detalles completos de un proyecto específico por su ID.
//     * Accesible por cualquier usuario que esté autenticado.
//     *
//     * @param id El ID del proyecto a buscar, extraído de la ruta de la URL.
//     * @return Una respuesta HTTP 200 (OK) con los detalles completos del proyecto, o 404 si no se encuentra.
//     */
//    @Operation(
//            summary = "Obtiene los detalles completos de un proyecto por su ID",
//            description = "Accesible por cualquier usuario autenticado.",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
//    @GetMapping("/{id}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Integer id) {
//        // @PathVariable: Esta anotación vincula el valor de la variable de la URI (en este caso, {id})
//        // al parámetro del método.
//        ProjectResponseDto project = projectService.getProjectById(id);
//        return ResponseEntity.ok(project);
//    }


    /*
     *  Endpoint para obtener una lista de los proyectos del usuario autenticado.
     *  Solo accesible por usuarios con el rol 'Desarrollador' y el usuario que creo el proyecto.
     * */
    @Operation(
            summary = "Obtiene una lista de los proyectos del usuario autenticado",
            description = "Accesible solo por un usuario con rol 'Desarrollador'. Devuelve una lista de resúmenes de sus propios proyectos.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/my-projects")
    @PreAuthorize("hasRole('Desarrollador')")
    public ResponseEntity<List<ProjectSummaryDto>> getMyProjects(Authentication authentication) {
        // 1. Obtenemos el objeto User completo desde el contexto de seguridad.
        User currentUser = (User) authentication.getPrincipal();
        // 2. Extraemos su ID numérico.
        Integer userId = currentUser.getId();
        // 3. Llamamos al servicio pasando el ID.
        List<ProjectSummaryDto> myProjects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(myProjects);
    }


    /*
     * Endpoint para editar un proyecto existente.
     * Solo accesible por usuarios con el rol 'Desarrollador' y el usuario que creó el proyecto.
     *
     *  @param id El ID del proyecto a actualizar.
     *  @param request El cuerpo de la petición con los datos actualizados del proyecto.
     *  @param authentication Objeto que contiene la información del usuario autenticado.
     *  @return Una respuesta HTTP 200 (OK) con los datos del proyecto actualizado.
     * */
    @Operation(
            summary = "Actualiza un proyecto existente",
            description = "Permite al propietario de un proyecto actualizar sus detalles. Se requiere ser el autor original.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Proyecto actualizado exitosamente")
    @ApiResponse(responseCode = "403", description = "Acceso denegado, no eres el propietario del proyecto")
    @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    @PutMapping("/{slug}")
    @PreAuthorize("@projectService.isOwnerBySlug(#slug, authentication.name)")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable String slug,
            @Valid @RequestBody CreateProjectRequestDto request,
            Authentication authentication) {

        ProjectResponseDto updatedProject = projectService.updateProjectBySlug(slug, request);
        return ResponseEntity.ok(updatedProject);
    }


    /*
     * Obtiene un proyecto por su slug.
     *  */
    @Operation(
            summary = "Obtiene los detalles completos de un proyecto por su Slug",
            description = "Accesible por cualquier usuario autenticado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    @GetMapping("/{slug}")
    @PreAuthorize("@projectService.canViewProject(#slug, authentication.name)")
    public ResponseEntity<ProjectResponseDto> getProjectBySlug(@PathVariable String slug) {
        ProjectResponseDto project = projectService.getProjectBySlug(slug);
        return ResponseEntity.ok(project);
    }


}