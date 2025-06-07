package com.incubadora.incubadora.dev.entity.common;


import com.incubadora.incubadora.dev.entity.job.JobOffer;
import com.incubadora.incubadora.dev.entity.project.Project;
import com.incubadora.incubadora.dev.entity.project.TeamProject;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Technology")
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "technologies")
    private Set<Project> projects = new HashSet<>(); // No se usa en la base de datos, pero se puede usar para mostrar los proyectos de una tecnología.

    @ManyToMany(mappedBy = "technologies")
    private Set<TeamProject> teamProjects = new HashSet<>();

    @ManyToMany(mappedBy = "technologies")
    private Set<JobOffer> jobOffers = new HashSet<>();

    // Constructores
    public Technology() {
    }

    public Technology(String name) {
        this.name = name;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<TeamProject> getTeamProjects() {
        return teamProjects;
    }

    public void setTeamProjects(Set<TeamProject> teamProjects) {
        this.teamProjects = teamProjects;
    }

    public Set<JobOffer> getJobOffers() {
        return jobOffers;
    }

    public void setJobOffers(Set<JobOffer> jobOffers) {
        this.jobOffers = jobOffers;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technology that = (Technology) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Technology{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
