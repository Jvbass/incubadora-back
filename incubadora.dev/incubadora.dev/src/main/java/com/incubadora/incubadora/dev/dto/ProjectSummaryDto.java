package com.incubadora.incubadora.dev.dto;

import java.sql.Timestamp;
import java.util.Set;

/**
 * DTO para mostrar una versión resumida de un proyecto en una lista.
 * Optimizado para no cargar datos innecesarios como la descripción completa.
 */
public class ProjectSummaryDto {
    private Integer id;
    private String slug;
    private String title;
    private String developerUsername;
    private Timestamp createdAt;
    private Set<TechnologyDto> technologies; // Colores de las tecnologías para la vista de lista
    private boolean isCollaborative;
    private boolean needMentoring = false; // Indica si el proyecto necesita mentoría
    private String status;
    private Byte developmentProgress;


    // Getters y Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDeveloperUsername() {
        return developerUsername;
    }

    public void setDeveloperUsername(String developerUsername) {
        this.developerUsername = developerUsername;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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

    public void setIsCollaborative(boolean collaborative) {
        this.isCollaborative = collaborative;
    }

    public Byte getDevelopmentProgress() {
        return developmentProgress;
    }

    public void setDevelopmentProgress(Byte developmentProgress) {
        this.developmentProgress = developmentProgress;

    }

    public boolean getNeedMentoring() {
        return needMentoring;
    }

    public void setNeedMentoring(boolean needMentoring) {
        this.needMentoring = needMentoring;
    }

    public Set<TechnologyDto> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<TechnologyDto> technologies) {
        this.technologies = technologies;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
