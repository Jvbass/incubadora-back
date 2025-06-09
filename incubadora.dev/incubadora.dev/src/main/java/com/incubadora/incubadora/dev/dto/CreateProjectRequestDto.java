package com.incubadora.incubadora.dev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }
    public String getProjectUrl() { return projectUrl; }
    public void setProjectUrl(String projectUrl) { this.projectUrl = projectUrl; }
    public List<Integer> getTechnologyIds() { return technologyIds; }
    public void setTechnologyIds(List<Integer> technologyIds) { this.technologyIds = technologyIds; }
    public List<Integer> getToolIds() { return toolIds; }
    public void setToolIds(List<Integer> toolIds) { this.toolIds = toolIds; }
}
