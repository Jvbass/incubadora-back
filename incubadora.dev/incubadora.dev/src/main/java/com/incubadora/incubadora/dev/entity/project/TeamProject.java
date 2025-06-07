package com.incubadora.incubadora.dev.entity.project;

import com.incubadora.incubadora.dev.entity.common.Profile;
import com.incubadora.incubadora.dev.entity.common.Technology;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackTeam;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TeamProject")
public class TeamProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "development_stage", length = 100)
    private String developmentStage;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "TeamProject_Technologies",
            joinColumns = @JoinColumn(name = "team_project_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private Set<Technology> technologies = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "TeamProject_Profiles",
            joinColumns = @JoinColumn(name = "team_project_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private Set<Profile> neededProfiles = new HashSet<>();

    @OneToMany(mappedBy = "teamProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamMember> members = new HashSet<>();

    @OneToMany(mappedBy = "teamProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedbackTeam> feedbacks = new HashSet<>();

    // Constructores
    public TeamProject() {
    }

    public TeamProject(User creator, String title, String description) {
        this.creator = creator;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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

    public String getDevelopmentStage() {
        return developmentStage;
    }

    public void setDevelopmentStage(String developmentStage) {
        this.developmentStage = developmentStage;
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

    public Set<Profile> getNeededProfiles() {
        return neededProfiles;
    }

    public void setNeededProfiles(Set<Profile> neededProfiles) {
        this.neededProfiles = neededProfiles;
    }

    public Set<TeamMember> getMembers() {
        return members;
    }

    public void setMembers(Set<TeamMember> members) {
        this.members = members;
    }

    public Set<FeedbackTeam> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<FeedbackTeam> feedbacks) {
        this.feedbacks = feedbacks;
    }

    // Métodos helper
    public void addTechnology(Technology technology) {
        this.technologies.add(technology);
        technology.getTeamProjects().add(this);
    }

    public void removeTechnology(Technology technology) {
        this.technologies.remove(technology);
        technology.getTeamProjects().remove(this);
    }

    public void addNeededProfile(Profile profile) {
        this.neededProfiles.add(profile);
        profile.getTeamProjects().add(this);
    }

    public void removeNeededProfile(Profile profile) {
        this.neededProfiles.remove(profile);
        profile.getTeamProjects().remove(this);
    }

    public void addMember(TeamMember member) {
        members.add(member);
        member.setTeamProject(this);
    }

    public void removeMember(TeamMember member) {
        members.remove(member);
        member.setTeamProject(null);
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamProject that = (TeamProject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TeamProject{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", creator=" + (creator != null ? creator.getUsername() : null) +
                '}';
    }
}

