package com.incubadora.incubadora.dev.repository;


import com.incubadora.incubadora.dev.entity.project.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    /**
     * Busca todos los proyectos y carga anticipadamente las relaciones especificadas
     * para evitar el problema N+1.
     *
     * @return Una lista de todos los proyectos con sus relaciones cargadas.
     */
    @EntityGraph(attributePaths = {"developer", "technologies"})
    // Carga anticipada de relaciones
    List<Project> findByStatus(String status);

    /**
     * Busca un proyecto por su ID y carga anticipadamente las relaciones especificadas.
     *
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
     *
     * @param username El nombre de usuario del desarrollador.
     * @return Una lista de proyectos del usuario.
     */
    @EntityGraph(attributePaths = {"developer", "technologies"})
    List<Project> findByDeveloper_Username(String username);

    /**
     * Busca todos los proyectos que pertenecen a un desarrollador específico,
     * identificado por su ID numérico. Carga las tecnologías de forma
     * anticipada para un rendimiento óptimo.
     *
     * @param developerId El ID del desarrollador.
     * @return Una lista de proyectos del usuario.
     */
    @EntityGraph(attributePaths = {"developer", "technologies"})
    List<Project> findByDeveloper_Id(Integer developerId);


    Optional<Project> findBySlug(String slug);

    /**
     * Busca una página de proyectos filtrando por su estado.
     * Carga anticipadamente las relaciones para evitar problemas de N+1.
     *
     * @param status   El estado del proyecto (ej. "published").
     * @param pageable Objeto que contiene la información de paginación (número de página, tamaño y orden).
     * @return Una página (Page) de proyectos.
     */
    @EntityGraph(attributePaths = {"developer", "technologies"})
    Page<Project> findByStatus(String status, Pageable pageable);


    /**
     * Busca proyectos publicados y los ordena por la cantidad de feedbacks recibidos
     * desde una fecha específica. Usa LEFT JOIN para incluir proyectos sin feedback.
     */
    /**
     * Busca proyectos publicados y los ordena por la cantidad de feedbacks recibidos
     * desde una fecha específica. Compatible con sql_mode=only_full_group_by.
     */
    @Query(value = "SELECT p FROM Project p WHERE p.id IN (" +
            "SELECT p2.id FROM Project p2 LEFT JOIN p2.feedbacks f ON f.createdAt >= :since " +
            "WHERE p2.status = 'published' " +
            "GROUP BY p2.id " +
            "ORDER BY COUNT(f) DESC, p2.createdAt DESC" +
            ")",
            countQuery = "SELECT count(p) FROM Project p WHERE p.status = 'published'") // Query de conteo para la paginación
    @EntityGraph(attributePaths = {"developer", "technologies"})
    Page<Project> findPublishedProjectsOrderByMostFeedbackSince(@Param("since") Timestamp since, Pageable pageable);

    /**
     * Proyectos - publicados - orden x rating promedio de feedbacks
     * recibidos desde una fecha específica.
     * Usa JOIN para incluir solo proyectos con al menos un feedback.
     */
    @Query(value = "SELECT p FROM Project p WHERE p.id IN (" +
            "SELECT p2.id FROM Project p2 JOIN p2.feedbacks f " +
            "WHERE p2.status = 'published' AND f.createdAt >= :since " +
            "GROUP BY p2.id " +
            "HAVING COUNT(f) > 0 " +
            "ORDER BY AVG(f.rating) DESC, COUNT(f) DESC, p2.createdAt DESC" +
            ")",
            countQuery = "SELECT count(p) FROM Project p JOIN p.feedbacks f WHERE p.status = 'published' AND f.createdAt >= :since") // Query de conteo
    @EntityGraph(attributePaths = {"developer", "technologies"})
    Page<Project> findPublishedProjectsOrderByTopRatingSince(@Param("since") Timestamp since, Pageable pageable);
}


