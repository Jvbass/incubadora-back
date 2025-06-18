package com.incubadora.incubadora.dev.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

/**
 * DTO para la petición de creación de un nuevo proyecto.
 */
public class CreateProjectRequestDto {

    @NotBlank(message = "El título no puede estar vacío.")
    @Size(max = 255, message = "El título no puede exceder los 255 caracteres.")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía.")
    private String description;

    @URL(message = "El enlace al repositorio debe ser una URL válida.")
    private String repositoryUrl; // Opcional

    @URL(message = "El enlace al proyecto debe ser una URL válida.")
    private String projectUrl; // Opcional

    @NotEmpty(message = "Debes especificar al menos una tecnología.")
    private List<Integer> technologyIds; // Lista de IDs de las tecnologías usadas

    @NotEmpty(message = "Debes especificar al menos una herramienta.")
    private List<Integer> toolIds; // Lista de IDs de las herramientas usadas

    @NotBlank(message = "El estado del proyecto no puede estar vacío.")
    @Pattern(regexp = "pending|published|archived")
    private String status;

    private boolean isCollaborative;

    @Min(value = 0, message = "El progreso del desarrollo no puede ser menor a 0")
    @Max(value = 100, message = "El progreso del desarrollo no puede ser mayor a 100")
    private Byte developmentProgress;

    // Getters y Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public List<Integer> getTechnologyIds() {
        return technologyIds;
    }

    public void setTechnologyIds(List<Integer> technologyIds) {
        this.technologyIds = technologyIds;
    }

    public List<Integer> getToolIds() {
        return toolIds;
    }

    public void setToolIds(List<Integer> toolIds) {
        this.toolIds = toolIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIsCollaborative() {
        return isCollaborative;
    }

    public void setIsCollaborative(boolean isCollaborative) {
        this.isCollaborative = isCollaborative;
    }

    public Byte getDevelopmentProgress() {
        return developmentProgress;
    }

    public void setDevelopmentProgress(Byte developmentProgress) {
        this.developmentProgress = developmentProgress;
    }
}
