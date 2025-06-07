package com.incubadora.incubadora.dev.entity.feedback;

import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.mentorship.MentorShipSession;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "FeedbackMentor")
public class FeedbackMentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorship_session_id", nullable = false)
    private MentorShipSession mentorshipSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_id", nullable = false) // Usuario (desarrollador) que da el feedback
    private User mentee; // El SQL lo define, así que lo mantenemos.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false) // Usuario (mentor) que recibe el feedback
    private User mentor;

    @Column(name = "rating") // TINYINT CHECK (rating >= 1 AND rating <= 5)
    private Byte rating; // Byte es adecuado para TINYINT y permite valores null si rating es opcional

    @Lob
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    // Constructores
    public FeedbackMentor() {
    }

    public FeedbackMentor(MentorShipSession mentorshipSession, User mentee, User mentor, Byte rating, String comment) {
        this.mentorshipSession = mentorshipSession;
        this.mentee = mentee;
        this.mentor = mentor;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MentorShipSession getMentorShipSession() {
        return mentorshipSession;
    }

    public void setMentorShipSession(MentorShipSession mentorshipSession) {
        this.mentorshipSession = mentorshipSession;
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

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        FeedbackMentor that = (FeedbackMentor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FeedbackMentor{" +
                "id=" + id +
                ", mentorshipSessionId=" + (mentorshipSession != null ? mentorshipSession.getId() : null) +
                ", mentee=" + (mentee != null ? mentee.getUsername() : null) +
                ", mentor=" + (mentor != null ? mentor.getUsername() : null) +
                ", rating=" + rating +
                '}';
    }
}

