package com.incubadora.incubadora.dev.entity.core;
import com.incubadora.incubadora.dev.entity.admin.News;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackMentor;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackProject;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackTeam;
import com.incubadora.incubadora.dev.entity.job.JobOffer;
import com.incubadora.incubadora.dev.entity.mentorship.MentorService;
import com.incubadora.incubadora.dev.entity.mentorship.MentorShipSession;
import com.incubadora.incubadora.dev.entity.project.Project;
import com.incubadora.incubadora.dev.entity.project.TeamMember;
import com.incubadora.incubadora.dev.entity.project.TeamProject;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "User") // 'User' es una palabra reservada en algunas BD, considera UserAccount o AppUser
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    // Implementación de los métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // El rol debe tener el prefijo "ROLE_" para que Spring Security lo reconozca
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    // Relaciones OneToMany (el usuario es el "uno")
    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> developedProjects = new HashSet<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamProject> createdTeamProjects = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamMember> teamMemberships = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedbackProject> givenProjectFeedbacks = new HashSet<>();

    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedbackTeam> givenTeamFeedbacks = new HashSet<>();

    @OneToMany(mappedBy = "reviewed", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedbackTeam> receivedTeamFeedbacks = new HashSet<>();

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MentorService> mentorServices = new HashSet<>();

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MentorShipSession> menteeSessions = new HashSet<>();

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MentorShipSession> mentorSessions = new HashSet<>();

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedbackMentor> givenMentorFeedbacks = new HashSet<>();

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedbackMentor> receivedMentorFeedbacks = new HashSet<>();

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobOffer> jobOffers = new HashSet<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<News> newsAuthored = new HashSet<>();


    // Constructores
    public User() {
    }

    public User(String username, String email, String passwordHash, Role role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Role getRole() {
        return role;
    }

    @Override
    public String getPassword() {
        return this.passwordHash; // Devuelve el hash de la contraseña
    }


    @Override
    public boolean isAccountNonExpired() {
        return true; // Podrías añadir lógica para cuentas que expiran
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Podrías añadir lógica para bloquear cuentas
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Podrías añadir lógica para credenciales que expiran
    }

    @Override
    public boolean isEnabled() {
        return true; // Podrías añadir un campo 'enabled' en tu entidad
    }

    // Este es el método helper que usamos en AuthService
    /**
     * Convierte la entidad User en un objeto UserDetails de Spring Security.
     * Este método es un "helper" para desacoplar nuestra entidad de dominio de cómo
     * el servicio JWT necesita construir el token.
     * Resuelve la necesidad de `user.toUserDetails()` en AuthService.
     *
     * @return un objeto UserDetails construido con los datos de este usuario.
     */
    public UserDetails toUserDetails() {
        return org.springframework.security.core.userdetails.User
                .withUsername(this.username)
                .password(this.passwordHash) // Aquí va el hash de la contraseña
                .authorities(getAuthorities()) // Usa el método que ya implementaste de UserDetails
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Project> getDevelopedProjects() {
        return developedProjects;
    }

    public void setDevelopedProjects(Set<Project> developedProjects) {
        this.developedProjects = developedProjects;
    }

    public Set<TeamProject> getCreatedTeamProjects() {
        return createdTeamProjects;
    }

    public void setCreatedTeamProjects(Set<TeamProject> createdTeamProjects) {
        this.createdTeamProjects = createdTeamProjects;
    }

    public Set<TeamMember> getTeamMemberships() {
        return teamMemberships;
    }

    public void setTeamMemberships(Set<TeamMember> teamMemberships) {
        this.teamMemberships = teamMemberships;
    }

    public Set<FeedbackProject> getGivenProjectFeedbacks() {
        return givenProjectFeedbacks;
    }

    public void setGivenProjectFeedbacks(Set<FeedbackProject> givenProjectFeedbacks) {
        this.givenProjectFeedbacks = givenProjectFeedbacks;
    }

    public Set<FeedbackTeam> getGivenTeamFeedbacks() {
        return givenTeamFeedbacks;
    }

    public void setGivenTeamFeedbacks(Set<FeedbackTeam> givenTeamFeedbacks) {
        this.givenTeamFeedbacks = givenTeamFeedbacks;
    }

    public Set<FeedbackTeam> getReceivedTeamFeedbacks() {
        return receivedTeamFeedbacks;
    }

    public void setReceivedTeamFeedbacks(Set<FeedbackTeam> receivedTeamFeedbacks) {
        this.receivedTeamFeedbacks = receivedTeamFeedbacks;
    }

    public Set<MentorService> getMentorServices() {
        return mentorServices;
    }

    public void setMentorServices(Set<MentorService> mentorServices) {
        this.mentorServices = mentorServices;
    }

    public Set<MentorShipSession> getMenteeSessions() {
        return menteeSessions;
    }

    public void setMenteeSessions(Set<MentorShipSession> menteeSessions) {
        this.menteeSessions = menteeSessions;
    }

    public Set<MentorShipSession> getMentorSessions() {
        return mentorSessions;
    }

    public void setMentorSessions(Set<MentorShipSession> mentorSessions) {
        this.mentorSessions = mentorSessions;
    }
    public Set<FeedbackMentor> getGivenMentorFeedbacks() {
        return givenMentorFeedbacks;
    }

    public void setGivenMentorFeedbacks(Set<FeedbackMentor> givenMentorFeedbacks) {
        this.givenMentorFeedbacks = givenMentorFeedbacks;
    }

    public Set<FeedbackMentor> getReceivedMentorFeedbacks() {
        return receivedMentorFeedbacks;
    }

    public void setReceivedMentorFeedbacks(Set<FeedbackMentor> receivedMentorFeedbacks) {
        this.receivedMentorFeedbacks = receivedMentorFeedbacks;
    }
    public Set<JobOffer> getJobOffers() {
        return jobOffers;
    }

    public void setJobOffers(Set<JobOffer> jobOffers) {
        this.jobOffers = jobOffers;
    }

    public Set<News> getNewsAuthored() {
        return newsAuthored;
    }

    public void setNewsAuthored(Set<News> newsAuthored) {
        this.newsAuthored = newsAuthored;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + (role != null ? role.getName() : null) +
                '}';
    }
}
