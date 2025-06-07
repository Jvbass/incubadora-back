package com.incubadora.incubadora.dev.entity.feedback;

import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.project.Project;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "FeedbackProject")
public class FeedbackProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Usuario que da el feedback
    private User user;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation", nullable = false)
    private EvaluationType evaluation; // LIKE, DISLIKE

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    public enum EvaluationType {
        LIKE, DISLIKE
    }

    // Constructores
    public FeedbackProject() {
    }

    public FeedbackProject(Project project, User user, String description, EvaluationType evaluation) {
        this.project = project;
        this.user = user;
        this.description = description;
        this.evaluation = evaluation;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EvaluationType getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(EvaluationType evaluation) {
        this.evaluation = evaluation;
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
        FeedbackProject that = (FeedbackProject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FeedbackProject{" +
                "id=" + id +
                ", project=" + (project != null ? project.getTitle() : null) +
                ", user=" + (user != null ? user.getUsername() : null) +
                ", evaluation=" + evaluation +
                '}';
    }
}
