package com.incubadora.incubadora.dev.entity.mentorship;

import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackMentor;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "MentorshipSession")
public class MentorShipSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_service_id", nullable = false)
    private MentorService mentorService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_id", nullable = false)
    private User mentee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false) // Se duplica el mentor aquí, ya está en MentorService. Podría quitarse si la lógica lo permite.
    private User mentor; // El SQL lo define, así que lo mantenemos.

    @Column(name = "session_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp sessionDate;

    @Column(name = "status", length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'solicitada'")
    private String status; // 'solicitada', 'confirmada', 'completada', 'cancelada'

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "mentorshipSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedbackMentor> feedbacks = new HashSet<>();


    // Constructores
    public MentorShipSession() {
    }

    public MentorShipSession(MentorService mentorService, User mentee, User mentor, String status) {
        this.mentorService = mentorService;
        this.mentee = mentee;
        this.mentor = mentor;
        this.status = status;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MentorService getMentorService() {
        return mentorService;
    }

    public void setMentorService(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    public User getMentee() {
        return mentee;
    }

    public void setMentee(User mentee) {
        this.mentee = mentee;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public Timestamp getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Timestamp sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Set<FeedbackMentor> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<FeedbackMentor> feedbacks) {
        this.feedbacks = feedbacks;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MentorShipSession that = (MentorShipSession) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MentorshipSession{" +
                "id=" + id +
                ", mentorServiceId=" + (mentorService != null ? mentorService.getId() : null) +
                ", mentee=" + (mentee != null ? mentee.getUsername() : null) +
                ", mentor=" + (mentor != null ? mentor.getUsername() : null) +
                ", status='" + status + '\'' +
                '}';
    }
}
