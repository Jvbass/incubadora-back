package com.incubadora.incubadora.dev.service;

import com.incubadora.incubadora.dev.dto.*;
import com.incubadora.incubadora.dev.enums.ProjectSortBy;
import com.incubadora.incubadora.dev.entity.common.Technology;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.project.Project;
import com.incubadora.incubadora.dev.exception.ResourceNotFoundException;
import com.incubadora.incubadora.dev.repository.ProjectRepository;
import com.incubadora.incubadora.dev.repository.TechnologyRepository;
import com.incubadora.incubadora.dev.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Optional;


/**
 * Servicio para gestionar las operaciones relacionadas con los proyectos.
 */

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TechnologyRepository technologyRepository;
    private final SlugService slugService;


    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository,
                          TechnologyRepository technologyRepository, SlugService slugService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
        this.slugService = slugService;
    }

    /**
     * =====================Crear proyecto================================
     *
     * @param request  DTO con los datos del proyecto
     * @param username Nombre de usuario del desarrollador que crea el proyecto
     * @return ProjectResponseDto DTO con los detalles del proyecto creado
     * =====================================================
     */
    @Transactional
    public ProjectResponseDto createProject(CreateProjectRequestDto request, String username) {
        // obtiene al usuario desarrollador que está creando el proyecto.
        User developer = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Busca las entidades de Tecnología y Herramienta por sus IDs.
        List<Technology> technologies = technologyRepository.findAllById(request.getTechnologyIds());
        if (technologies.size() != request.getTechnologyIds().size()) {
            throw new IllegalArgumentException("Una o más tecnologías especificadas no son válidas.");
        }

        // Crea un nuevo objeto de Proyecto.
        Project newProject = new Project();
        newProject.setTitle(request.getTitle());
        newProject.setDescription(request.getDescription());
        newProject.setDeveloper(developer);
        newProject.setRepositoryUrl(request.getRepositoryUrl());
        newProject.setProjectUrl(request.getProjectUrl());
        newProject.setStatus(request.getStatus());
        newProject.setIsCollaborative(request.getIsCollaborative());
        newProject.setNeedMentoring(request.getNeedMentoring());
        newProject.setDevelopmentProgress(request.getDevelopmentProgress());

        // Genera un slug único basado en el título del proyecto.
        String uniqueSlug = slugService.generateUniqueSlug(request.getTitle());
        newProject.setSlug(uniqueSlug);

        // Asigna las tecnologías y herramientas.
        newProject.setTechnologies(new HashSet<>(technologies));
        // Guarda el proyecto en la base de datos.
        Project savedProject = projectRepository.save(newProject);

        // Mapea la entidad guardada a un DTO de respuesta y lo devuelve.
        return mapToProjectResponseDto(savedProject);
    }


    /**
     * ========================Actualiza un proyecto=============================
     * Actualiza un proyecto existente con los datos proporcionados.
     * La verificación de propiedad ya se ha realizado mediante @PreAuthorize en el controlador.
     *
     * @param slug    El slug del proyecto a actualizar.
     * @param request El DTO con los datos actualizados del proyecto.
     * @return ProjectResponseDto DTO con los detalles del proyecto actualizado.
     * =====================================================
     */
    @Transactional
    public ProjectResponseDto updateProjectBySlug(String slug, CreateProjectRequestDto request) {
        // 1. Buscar el proyecto. Si no se encuentra, lanza una excepción.
        Project projectToUpdate = projectRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con nombre: " + slug));

        // 2. Actualizar los campos del proyecto con los datos del DTO.
        projectToUpdate.setTitle(request.getTitle());
        projectToUpdate.setDescription(request.getDescription());
        projectToUpdate.setRepositoryUrl(request.getRepositoryUrl());
        projectToUpdate.setProjectUrl(request.getProjectUrl());
        projectToUpdate.setStatus(request.getStatus());
        projectToUpdate.setIsCollaborative(request.getIsCollaborative());
        projectToUpdate.setNeedMentoring(request.getNeedMentoring());
        projectToUpdate.setDevelopmentProgress(request.getDevelopmentProgress());

        // 3. Manejar la actualización de las tecnologías.
        if (request.getTechnologyIds() != null && !request.getTechnologyIds().isEmpty()) {
            List<Technology> technologies = technologyRepository.findAllById(request.getTechnologyIds());
            if (technologies.size() != request.getTechnologyIds().size()) {
                throw new IllegalArgumentException("Una o más tecnologías especificadas no son válidas.");
            }
            projectToUpdate.setTechnologies(new HashSet<>(technologies));
        }

        // 4. Guardar los cambios en la base de datos.
        Project savedProject = projectRepository.save(projectToUpdate);

        // 5. Mapear la entidad actualizada al DTO de respuesta y devolverlo.
        return mapToProjectResponseDto(savedProject);
    }


    /*=====================================================
     *   Obtener todos los proyectos de un usuario por su nombre de usuario (Resumen de los proyectos ProjectSummaryDto)
     * @param username Nombre de usuario del desarrollador
     * @return Lista de ProjectSummaryDto con los proyectos del desarrollador
     *======================================================*/
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getProjectsByUsername(String username) {
        return projectRepository.findByDeveloper_Username(username).stream()
                .map(this::mapToProjectSummaryDto)
                .collect(Collectors.toList());
    }


    /*======================================================*
     * Obtiene el detalle de un proyecto por su Slug
     * @param slug Slug del proyecto
     * @return ProjectResponseDto con los detalles del proyecto
     *======================================================* */
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectBySlug(String slug) {
        Project project = projectRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con slug: " + slug));
        return mapToProjectResponseDto(project);
    }


    /**
     * ======================================================*
     * Obtiene todos los proyectos en estado publicados.
     *
     * @return Lista de ProjectSummaryDto con los proyectos publicados.
     * ======================================================
     */
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getAllPublishedProjects() {
        // Usamos el nuevo método del repositorio para filtrar en la base de datos
        return projectRepository.findByStatus("published").stream()
                .map(this::mapToProjectSummaryDto) // Mapeamos cada proyecto a su DTO
                .collect(Collectors.toList()); // Collectors.toList() convierte el Stream en una lista
    }

    /**
     * Obtiene una lista paginada de todos los proyectos publicados.
     *
     * @param page El número de página a solicitar (empezando desde 0).
     * @param size El tamaño de la página (cuántos proyectos por página).
     * @return Un DTO con la lista de proyectos y la información de paginación.
     */
    @Transactional(readOnly = true)
    public PagedResponseDto<ProjectSummaryDto> getPublishedProjects(int page, int size, ProjectSortBy sortBy) {
        Page<Project> projectPage;
        Pageable pageable = PageRequest.of(page, size); // El orden se define en la consulta, no aquí

        projectPage = switch (sortBy) {
            case MOST_FEEDBACK -> {
                Timestamp sevenDaysAgoFeedback = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
                yield projectRepository.findPublishedProjectsOrderByMostFeedbackSince(sevenDaysAgoFeedback, pageable);
            }
            case TOP_RATED -> {
                Timestamp sevenDaysAgoRating = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
                yield projectRepository.findPublishedProjectsOrderByTopRatingSince(sevenDaysAgoRating, pageable);
            }
            default -> {
                // Para 'LATEST', sí definimos el orden aquí.
                Pageable latestPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                yield projectRepository.findByStatus("published", latestPageable);
            }
        };

        List<ProjectSummaryDto> content = projectPage.getContent().stream()
                .map(this::mapToProjectSummaryDto)
                .collect(Collectors.toList());

        return new PagedResponseDto<>(
                content,
                projectPage.getNumber(),
                projectPage.getSize(),
                projectPage.getTotalElements(),
                projectPage.getTotalPages(),
                projectPage.isLast()
        );
    }


    /**
     * ================================================
     * Obtener todos los proyectos de un usuario por su ID (Resumen de los proyectos ProjectSummaryDto)
     *
     * @param userId ID del desarrollador
     * @return Lista de ProjectSummaryDto con los proyectos del desarrollador
     * ===============================================
     */
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getProjectsByUserId(Integer userId) {
        return projectRepository.findByDeveloper_Id(userId).stream()
                .map(this::mapToProjectSummaryDto)
                .collect(Collectors.toList());
    }


    /**
     * ======================================================*
     * helper para mapear a DTO de proyectos resumidos
     *
     * @param project Entidad de proyecto a mapear
     * @return ProjectSummaryDto con los datos del proyecto
     * ======================================================
     **/
    private ProjectSummaryDto mapToProjectSummaryDto(Project project) {
        ProjectSummaryDto dto = new ProjectSummaryDto();
        dto.setId(project.getId());
        dto.setSlug(project.getSlug());
        dto.setTitle(project.getTitle());
        dto.setDeveloperUsername(project.getDeveloper().getUsername());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setStatus(project.getStatus());
        dto.setIsCollaborative(project.getIsCollaborative());
        dto.setNeedMentoring(project.getNeedMentoring());
//        dto.setRepositoryUrl(project.getRepositoryUrl());
        dto.setDevelopmentProgress(project.getDevelopmentProgress());

        dto.setTechnologies(project.getTechnologies().stream()
                .map(tech -> new TechnologyDto(tech.getId(), tech.getName(), tech.getTechColor()))
                .collect(Collectors.toSet()));
        return dto;
    }


    /*======================================================
     * helper para mapear de Entidad a DTO
     * ======================================================**/
    private ProjectResponseDto mapToProjectResponseDto(Project project) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(project.getId());
        dto.setSlug(project.getSlug());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setRepositoryUrl(project.getRepositoryUrl());
        dto.setProjectUrl(project.getProjectUrl());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setDeveloperUsername(project.getDeveloper().getUsername());
        dto.setStatus(project.getStatus());
        dto.setIsCollaborative(project.getIsCollaborative());
        dto.setNeedMentoring(project.getNeedMentoring());
        dto.setDevelopmentProgress(project.getDevelopmentProgress());

        dto.setTechnologies(project.getTechnologies().stream()
                .map(tech -> new TechnologyDto(tech.getId(), tech.getName(), tech.getTechColor()))
                .collect(Collectors.toSet()));

        return dto;
    }


    /**
     * Método de autorización para ser usado por Spring Security (@PreAuthorize) desde el controlador.
     * Verifica si el usuario autenticado es el propietario del proyecto.
     *
     * @param slug     El ID del proyecto a verificar.
     * @param username El nombre del usuario que intenta acceder.
     * @return true si el usuario es el propietario, false en caso contrario.
     */
    @Transactional(readOnly = true)
    public boolean isOwnerBySlug(String slug, String username) {
        // Usamos findById directamente. Si no existe el proyecto, no hay propietario.
        return projectRepository.findBySlug(slug)
                // Mapeamos el Optional<Project> a un Optional<Boolean>
                .map(project -> project.getDeveloper().getUsername().equals(username))
                // Si el Optional está vacío (proyecto no encontrado), devolvemos false.
                .orElse(false);
    }


    /**
     * Verifica si un usuario tiene permiso para ver un proyecto.
     * Se usa en @PreAuthorize para proteger el endpoint.
     * Reglas:
     * 1. Si el proyecto está 'published', cualquiera puede verlo.
     * 2. Si no está 'published', solo el autor puede verlo.
     *
     * @param slug     El slug del proyecto a verificar.
     * @param username El nombre del usuario autenticado (puede ser nulo si es anónimo).
     * @return true si el usuario tiene permiso, false en caso contrario.
     */
    @Transactional(readOnly = true)
    public boolean canViewProject(String slug, String username) {
        Optional<Project> projectOpt = projectRepository.findBySlug(slug);

        // Si el proyecto no existe, permitimos que el controlador continúe
        // para que pueda lanzar un error 404 Not Found, que es más preciso.
        if (projectOpt.isEmpty()) {
            return true;
        }

        Project project = projectOpt.get();

        // Regla 1: El proyecto es público.
        if ("published".equalsIgnoreCase(project.getStatus())) {
            return true;
        }

        // Regla 2: El proyecto no es público, verificar si el usuario es el dueño.
        // `username` viene del contexto de seguridad y es confiable.
        if (username != null && project.getDeveloper().getUsername().equals(username)) {
            return true;
        }

        // Si ninguna regla se cumple, se deniega el acceso.
        return false;
    }

}