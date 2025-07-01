package com.incubadora.incubadora.dev.entity.project;

import com.incubadora.incubadora.dev.entity.common.Technology;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackProject;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id", nullable = false)
    private User developer;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "repository_url", length = 100)
    private String repositoryUrl;

    @Column(name = "project_url", length = 100)
    private String projectUrl;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "project_status", length = 15, columnDefinition = "VARCHAR(50) DEFAULT 'pending'")
    private String status; // 'pending', 'published', 'archived'

    @Column(name = "is_collaborative", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isCollaborative = false;

    @Column(name = "need_mentoring", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean needMentoring = false;

    @Min(value = 0, message = "El progreso del desarrollo no puede ser menor a 0")
    @Max(value = 100, message = "El progreso del desarrollo no puede ser mayor a 100")
    @Column(name = "development_progress", columnDefinition = "TINYINT DEFAULT 100")
    private Byte developmentProgress = 100;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "Project_Technologies",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private Set<Technology> technologies = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedbackProject> feedbacks = new HashSet<>();

    // Constructores
    public Project() {
    }

    public Project(User developer, String title, String description) {
        this.developer = developer;
        this.title = title;
        this.description = description;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getDeveloper() {
        return developer;
    }

    public void setDeveloper(User developer) {
        this.developer = developer;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIsCollaborative(boolean isCollaborative) {
        this.isCollaborative = isCollaborative;
    }

    public boolean getIsCollaborative() {
        return isCollaborative;
    }

    public Byte getDevelopmentProgress() {
        return developmentProgress;
    }

    public void setDevelopmentProgress(Byte developmentProgress) {
        this.developmentProgress = developmentProgress;
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

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }


    public Set<FeedbackProject> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<FeedbackProject> feedbacks) {
        this.feedbacks = feedbacks;
    }


    // Métodos helper para manejar relaciones ManyToMany
    public void addTechnology(Technology technology) {
        this.technologies.add(technology);
        technology.getProjects().add(this);
    }

    public void removeTechnology(Technology technology) {
        this.technologies.remove(technology);
        technology.getProjects().remove(this);
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", developer=" + (developer != null ? developer.getUsername() : null) +
                '}';
    }

    public boolean getNeedMentoring() {
        return needMentoring;
    }

    public void setNeedMentoring(boolean needMentoring) {
        this.needMentoring = needMentoring;
    }
}
