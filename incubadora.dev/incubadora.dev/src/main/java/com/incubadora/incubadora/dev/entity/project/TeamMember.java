package com.incubadora.incubadora.dev.entity.project;

import com.incubadora.incubadora.dev.entity.core.User;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "TeamProject_Members")
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O usar una clave compuesta si prefieres
    private Integer id; // Añadido un ID autoincremental simple. Si prefieres clave compuesta mira abajo.

    /* // Si prefieres una clave compuesta (requiere una clase @EmbeddableId)
    @EmbeddedId
    private TeamMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamProjectId") // Hace referencia al atributo en TeamMemberId
    @JoinColumn(name = "team_project_id")
    private TeamProject teamProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId") // Hace referencia al atributo en TeamMemberId
    @JoinColumn(name = "member_id")
    private User member;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_project_id", nullable = false)
    private TeamProject teamProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Column(name = "role_in_project", length = 100)
    private String roleInProject;

    @Column(name = "joined_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp joinedAt;

    // Constructores
    public TeamMember() {
    }

    public TeamMember(TeamProject teamProject, User member, String roleInProject) {
        this.teamProject = teamProject;
        this.member = member;
        this.roleInProject = roleInProject;
        this.joinedAt = new Timestamp(System.currentTimeMillis());
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

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public String getRoleInProject() {
        return roleInProject;
    }

    public void setRoleInProject(String roleInProject) {
        this.roleInProject = roleInProject;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamMember that = (TeamMember) o;
        // Si usas ID simple
        return Objects.equals(id, that.id);
        // Si usas clave compuesta TeamMemberId:
        // return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // Si usas ID simple
        return Objects.hash(id);
        // Si usas clave compuesta TeamMemberId:
        // return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TeamMember{" +
                "id=" + id + // o this.id si es compuesta
                ", teamProject=" + (teamProject != null ? teamProject.getId() : null) +
                ", member=" + (member != null ? member.getUsername() : null) +
                ", roleInProject='" + roleInProject + '\'' +
                ", joinedAt=" + joinedAt +
                '}';
    }
}
