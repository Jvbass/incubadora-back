package com.incubadora.incubadora.dev.entity.feedback;

import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.project.TeamProject;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "FeedbackTeam", uniqueConstraints = {
        // Opcional: Podrías añadir una restricción para que un usuario no se dé feedback a sí mismo en el mismo proyecto.
        // La restricción chk_different_users_team se maneja a nivel de BD, aquí es más bien lógica de negocio.
})
public class FeedbackTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_project_id", nullable = false)
    private TeamProject teamProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false) // Usuario que da el feedback
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_id", nullable = false) // Usuario que recibe el feedback
    private User reviewed;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    // Constructores
    public FeedbackTeam() {
    }

    public FeedbackTeam(TeamProject teamProject, User reviewer, User reviewed, String title, String description) {
        this.teamProject = teamProject;
        this.reviewer = reviewer;
        this.reviewed = reviewed;
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

    public TeamProject getTeamProject() {
        return teamProject;
    }

    public void setTeamProject(TeamProject teamProject) {
        this.teamProject = teamProject;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getReviewed() {
        return reviewed;
    }

    public void setReviewed(User reviewed) {
        this.reviewed = reviewed;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackTeam that = (FeedbackTeam) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FeedbackTeam{" +
                "id=" + id +
                ", teamProject=" + (teamProject != null ? teamProject.getTitle() : null) +
                ", reviewer=" + (reviewer != null ? reviewer.getUsername() : null) +
                ", reviewed=" + (reviewed != null ? reviewed.getUsername() : null) +
                ", title='" + title + '\'' +
                '}';
    }
}
