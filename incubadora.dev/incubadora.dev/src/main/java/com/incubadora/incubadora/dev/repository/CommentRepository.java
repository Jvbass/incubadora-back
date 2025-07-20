package com.incubadora.incubadora.dev.repository;

import com.incubadora.incubadora.dev.entity.feedback.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Busca todos los comentarios de primer nivel (aquellos que no son respuestas)
     * para un FeedbackProject específico, ordenados por fecha de creación.
     *
     * Usa @EntityGraph para cargar de forma anticipada al autor y las respuestas (y los autores de esas respuestas),
     * previniendo el problema de N+1 consultas y mejorando el rendimiento.
     *
     * @param feedbackProjectId El ID del FeedbackProject.
     * @return Una lista de comentarios principales con sus respuestas cargadas.
     */
    @EntityGraph(attributePaths = { "author", "replies", "replies.author" })
    List<Comment> findByFeedbackProjectIdAndParentCommentIsNullOrderByCreatedAtAsc(Integer feedbackProjectId);
}