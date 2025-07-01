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


    // Obtener todos los proyectos ---
    @Transactional(readOnly = true)
    public List<ProjectSummaryDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToProjectSummaryDto)
                .collect(Collectors.toList());
    }

    //  Obtener un proyecto por ID (detalle completo) ---
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + projectId));
        return mapToProjectResponseDto(project);
    }


    // helper para mapear a DTO de resumen ---
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

        dto.setTechnologyNames(project.getTechnologies().stream()
                .map(Technology::getName)
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
}