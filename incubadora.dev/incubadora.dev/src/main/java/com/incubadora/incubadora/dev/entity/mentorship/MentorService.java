package com.incubadora.incubadora.dev.entity.mentorship;

import com.incubadora.incubadora.dev.entity.core.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "MentorService")
public class MentorService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "specialty", length = 255)
    private String specialty;

    @Lob
    @Column(name = "schedule_details", columnDefinition = "TEXT")
    private String scheduleDetails;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_free", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isFree = false;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "mentorService", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MentorShipSession> sessions = new HashSet<>();

    // Constructores
    public MentorService() {
    }

    public MentorService(User mentor, String title, String description, BigDecimal price, Boolean isFree) {
        this.mentor = mentor;
        this.title = title;
        this.description = description;
        this.price = price;
        this.isFree = isFree;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
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

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getScheduleDetails() {
        return scheduleDetails;
    }

    public void setScheduleDetails(String scheduleDetails) {
        this.scheduleDetails = scheduleDetails;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        isFree = free;
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

    public Set<MentorShipSession> getSessions() {
        return sessions;
    }

    public void setSessions(Set<MentorShipSession> sessions) {
        this.sessions = sessions;
    }

    public void addSession(MentorShipSession session) {
        this.sessions.add(session);
        session.setMentorService(this);
    }

    public void removeSession(MentorShipSession session) {
        this.sessions.remove(session);
        session.setMentorService(null);
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MentorService that = (MentorService) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MentorService{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", mentor=" + (mentor != null ? mentor.getUsername() : null) +
                ", price=" + price +
                ", isFree=" + isFree +
                '}';
    }
}
