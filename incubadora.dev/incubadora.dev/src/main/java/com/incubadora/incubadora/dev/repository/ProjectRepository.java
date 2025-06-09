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
    @EntityGraph(attributePaths = {"developer", "technologies", "tools"})
    List<Project> findAll();

    /**
     * Busca un proyecto por su ID y carga anticipadamente las relaciones especificadas.
     * @param id El ID del proyecto a buscar.
     * @return Un Optional que contiene el proyecto con sus detalles completos si se encuentra.
     */
    @Override
    @EntityGraph(attributePaths = {"developer", "technologies", "tools"})
    Optional<Project> findById(Integer id);
}

