package com.incubadora.incubadora.dev.dto;


import java.sql.Timestamp;
import java.util.Set;

/**
 * DTO para la respuesta al crear o solicitar un proyecto.
 * Muestra la información completa y legible.
 */
public class ProjectResponseDto {
    private Integer id;
    private String slug;
    private String title;
    private String description;
    private String repositoryUrl;
    private String projectUrl;
    private Timestamp createdAt;
    private String developerUsername;
    private Set<TechnologyDto> technologies;
    private String status;
    private Boolean isCollaborative;
    private Boolean needMentoring = false;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeveloperUsername() {
        return developerUsername;
    }

    public void setDeveloperUsername(String developerUsername) {
        this.developerUsername = developerUsername;
    }

    public Set<TechnologyDto> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<TechnologyDto> technologies) {
        this.technologies = technologies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsCollaborative() {
        return isCollaborative;
    }

    public void setIsCollaborative(Boolean isCollaborative) {
        this.isCollaborative = isCollaborative;
    }

    public Byte getDevelopmentProgress() {
        return developmentProgress;
    }

    public void setDevelopmentProgress(Byte developmentProgress) {
        this.developmentProgress = developmentProgress;
    }

    public Boolean getNeedMentoring() {
        return needMentoring;
    }

    public void setNeedMentoring(Boolean needMentoring) {
        this.needMentoring = needMentoring;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}