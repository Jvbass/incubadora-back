package com.incubadora.incubadora.dev.repository;

import com.incubadora.incubadora.dev.entity.feedback.FeedbackProject;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackProjectRepository extends JpaRepository<FeedbackProject, Integer> {

    /**
     * Busca todos los feedbacks para un proyecto específico, cargando el autor
     * de cada feedback de forma anticipada para evitar el problema N+1.
     * @param projectId El ID del proyecto.
     * @return Una lista de feedbacks para el proyecto dado.
     */
    @EntityGraph(attributePaths = {"author"})
    List<FeedbackProject> findByProjectId(Integer projectId);
}
