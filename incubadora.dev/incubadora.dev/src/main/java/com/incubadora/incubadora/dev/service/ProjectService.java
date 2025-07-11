package com.incubadora.incubadora.dev.service;

import com.incubadora.incubadora.dev.dto.*;
import com.incubadora.incubadora.dev.entity.common.Technology;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.project.Project;
import com.incubadora.incubadora.dev.exception.ResourceNotFoundException;
import com.incubadora.incubadora.dev.repository.ProjectRepository;
import com.incubadora.incubadora.dev.repository.TechnologyRepository;
import com.incubadora.incubadora.dev.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/*
 *  Servicio para tomar la petición, encontrar al usuario, las tecnologías y las herramientas, y crear el nuevo proyecto.
 * - Buscar al usuario desarrollador por su nombre de usuario.
 * - Buscar las tecnologías y herramientas por sus IDs.
 * - Crear una nueva entidad de Proyecto con los datos proporcionados.
 * - Asignar las tecnologías y herramientas al proyecto.
 * - Guardar el proyecto en la base de datos.
 *
 * */

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TechnologyRepository technologyRepository;


    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, TechnologyRepository technologyRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
    }


    //crear proyecto
    @Transactional
    public ProjectResponseDto createProject(CreateProjectRequestDto request, String username) {
        // 1. Buscar al usuario desarrollador que está creando el proyecto.
        User developer = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // 2. Buscar las entidades de Tecnología y Herramienta por sus IDs.
        List<Technology> technologies = technologyRepository.findAllById(request.getTechnologyIds());
        if (technologies.size() != request.getTechnologyIds().size()) {
            throw new IllegalArgumentException("Una o más tecnologías especificadas no son válidas.");
        }

        // 3. Crear la nueva entidad de Proyecto.
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


        // 4. Asignar las tecnologías y herramientas.
        newProject.setTechnologies(new HashSet<>(technologies));
        // 5. Guardar el proyecto en la base de datos.
        Project savedProject = projectRepository.save(newProject);

        // 6. Mapear la entidad guardada a un DTO de respuesta y devolverlo.
        return mapToProjectResponseDto(savedProject);
    }


    // Actualizar proyecto

    /**
     * Actualiza un proyecto existente con los datos proporcionados.
     * La verificación de propiedad ya se ha realizado mediante @PreAuthorize.
     *
     * @param projectId El ID del proyecto a actualizar.
     * @param request DTO con los nuevos datos.
     * @return DTO con la información del proyecto actualizado.
     */
    @Transactional
    public ProjectResponseDto updateProject(Integer projectId, CreateProjectRequestDto request) {
        // 1. Buscar el proyecto. Si no se encuentra, lanza una excepción.
        Project projectToUpdate = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + projectId));

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


    /* *Obtener todos los proyectos (Resumen de los proyectos ProjectSummaryDto)
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToProjectSummaryDto)
                .collect(Collectors.toList());
    }**/

    //  Obtener un proyecto por ID (detalle del proyecto completo ProjectResponseDto)
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + projectId));
        return mapToProjectResponseDto(project);
    }

    // Obtener todos los proyectos de un usuario por su nombre de usuario (Resumen de los proyectos ProjectSummaryDto)
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getProjectsByUsername(String username) {
        return projectRepository.findByDeveloper_Username(username).stream()
                .map(this::mapToProjectSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los proyectos en estado publicados.
     * @return Una lista de DTOs de proyectos  publicados .
     */
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getAllPublishedProjects() {
        // Usamos el nuevo método del repositorio para filtrar en la base de datos
        return projectRepository.findByStatus("published").stream()
                .map(this::mapToProjectSummaryDto) // Mapeamos cada proyecto a su DTO
                .collect(Collectors.toList()); // Collectors.toList() convierte el Stream en una lista

    }


    // Obtener todos los proyectos de un usuario por su ID ---
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getProjectsByUserId(Integer userId) {
        return projectRepository.findByDeveloper_Id(userId).stream()
                .map(this::mapToProjectSummaryDto)
                .collect(Collectors.toList());
    }


    // helper para mapear a DTO de proyectos resumidos ---
    private ProjectSummaryDto mapToProjectSummaryDto(Project project) {
        ProjectSummaryDto dto = new ProjectSummaryDto();
        dto.setId(project.getId());
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

    //helper para mapear de Entidad a DTO
    private ProjectResponseDto mapToProjectResponseDto(Project project) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(project.getId());
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
     * Método de autorización para ser usado por Spring Security (@PreAuthorize).
     * Verifica si el usuario autenticado es el propietario del proyecto.
     * @param projectId El ID del proyecto a verificar.
     * @param username El nombre del usuario que intenta acceder.
     * @return true si el usuario es el propietario, false en caso contrario.
     */
    @Transactional(readOnly = true)
    public boolean isOwner(Integer projectId, String username) {
        // Usamos findById directamente. Si no existe el proyecto, no hay propietario.
        return projectRepository.findById(projectId)
                // Mapeamos el Optional<Project> a un Optional<Boolean>
                .map(project -> project.getDeveloper().getUsername().equals(username))
                // Si el Optional está vacío (proyecto no encontrado), devolvemos false.
                .orElse(false);
    }


}