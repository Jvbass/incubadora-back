package com.incubadora.incubadora.dev.dto;

import java.sql.Timestamp;
import java.util.Set;

/**
 * DTO para mostrar una versión resumida de un proyecto en una lista.
 * Optimizado para no cargar datos innecesarios como la descripción completa.
 */
public class ProjectSummaryDto {
    private Integer id;
    private String title;
    private String developerUsername;
    private Timestamp createdAt;
    private Set<String> technologyNames; // Solo los nombres para la vista de lista
    private Set<String> toolNames;       // Solo los nombres para la vista de lista

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDeveloperUsername() { return developerUsername; }
    public void setDeveloperUsername(String developerUsername) { this.developerUsername = developerUsername; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Set<String> getTechnologyNames() { return technologyNames; }
    public void setTechnologyNames(Set<String> technologyNames) { this.technologyNames = technologyNames; }
    public Set<String> getToolNames() { return toolNames; }
    public void setToolNames(Set<String> toolNames) { this.toolNames = toolNames; }
}
