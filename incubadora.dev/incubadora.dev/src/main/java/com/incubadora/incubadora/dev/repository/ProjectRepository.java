package com.incubadora.incubadora.dev.repository;


import com.incubadora.incubadora.dev.entity.project.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    /**
     * Busca todos los proyectos y carga anticipadamente las relaciones especificadas
     * para evitar el problema N+1.
     * @return Una lista de todos los proyectos con sus relaciones cargadas.
     */
    @Override
    @EntityGraph(attributePaths = {"developer", "technologies"}) // Carga anticipada de relaciones
    List<Project> findAll();

    /**
     * Busca un proyecto por su ID y carga anticipadamente las relaciones especificadas.
     * @param id El ID del proyecto a buscar.
     * @return Un Optional que contiene el proyecto con sus detalles completos si se encuentra.
     */
    @Override
    @EntityGraph(attributePaths = {"developer", "technologies"})
    Optional<Project> findById(Integer id);

    /**
     * Busca todos los proyectos que pertenecen a un desarrollador específico,
     * identificado por su nombre de usuario. Carga las tecnologías de forma
     * anticipada para un rendimiento óptimo.
     * @param username El nombre de usuario del desarrollador.
     * @return Una lista de proyectos del usuario.
     */
    @EntityGraph(attributePaths = {"developer", "technologies"})
    List<Project> findByDeveloper_Username(String username);

    /**
     * Busca todos los proyectos que pertenecen a un desarrollador específico,
     * identificado por su ID numérico. Carga las tecnologías de forma
     * anticipada para un rendimiento óptimo.
     * @param developerId El ID del desarrollador.
     * @return Una lista de proyectos del usuario.
     */
    @EntityGraph(attributePaths = {"developer", "technologies"})
    List<Project> findByDeveloper_Id(Integer developerId);
}

