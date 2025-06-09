package com.incubadora.incubadora.dev.dto;


import java.sql.Timestamp;
import java.util.Set;

/**
 * DTO para la respuesta al crear o solicitar un proyecto.
 * Muestra la información completa y legible.
 */
public class ProjectResponseDto {
    private Integer id;
    private String title;
    private String description;
    private String repositoryUrl;
    private String projectUrl;
    private Timestamp createdAt;
    private String developerUsername;
    private Set<TechnologyDto> technologies;
    private Set<ToolDto> tools;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }
    public String getProjectUrl() { return projectUrl; }
    public void setProjectUrl(String projectUrl) { this.projectUrl = projectUrl; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public String getDeveloperUsername() { return developerUsername; }
    public void setDeveloperUsername(String developerUsername) { this.developerUsername = developerUsername; }
    public Set<TechnologyDto> getTechnologies() { return technologies; }
    public void setTechnologies(Set<TechnologyDto> technologies) { this.technologies = technologies; }
    public Set<ToolDto> getTools() { return tools; }
    public void setTools(Set<ToolDto> tools) { this.tools = tools; }
}