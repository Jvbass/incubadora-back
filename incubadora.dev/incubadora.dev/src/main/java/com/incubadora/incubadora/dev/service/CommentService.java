package com.incubadora.incubadora.dev.service;


import com.incubadora.incubadora.dev.dto.AuthorDto;
import com.incubadora.incubadora.dev.dto.CommentResponseDto;
import com.incubadora.incubadora.dev.dto.CreateCommentRequestDto;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.feedback.Comment;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackProject;
import com.incubadora.incubadora.dev.exception.ResourceNotFoundException;
import com.incubadora.incubadora.dev.mapper.CommentMapper;
import com.incubadora.incubadora.dev.repository.CommentRepository;
import com.incubadora.incubadora.dev.repository.FeedbackProjectRepository;
import com.incubadora.incubadora.dev.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * CommentService
 * Este servicio maneja las operaciones relacionadas con los comentarios en proyectos de feedback.
 * Permite obtener comentarios, crear nuevos comentarios y gestionar respuestas a comentarios.
 * Utiliza repositorios para interactuar con la base de datos y realizar operaciones CRUD.
 *
 * @author Incubadora Team
 * @version 1.0
 * Servicio para manejar operaciones relacionadas con comentarios en proyectos de feedback.
 */

@Service
public class CommentService {

    // Repositorios para acceder a los datos de comentarios, proyectos de feedback y usuarios
    private final CommentRepository commentRepository;
    private final FeedbackProjectRepository feedbackProjectRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    // Constructor para inyectar las dependencias de los repositorios
    public CommentService(CommentRepository commentRepository,
                          FeedbackProjectRepository feedbackProjectRepository,
                          UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.feedbackProjectRepository = feedbackProjectRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }


    /**
     * =======================================
     * Obtiene todos los comentarios de un feedback project específico.
     * =======================================
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsForFeedback(Integer feedbackId) {
        if (!feedbackProjectRepository.existsById(feedbackId)) {
            throw new IllegalArgumentException("Feedback no encontrado con Id: " + feedbackId);
        }
        List<Comment> topLevelComments = commentRepository.findByFeedbackProjectIdAndParentCommentIsNullOrderByCreatedAtAsc(feedbackId);
        return topLevelComments.stream()
                .map(commentMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    /**
     * ===============================
     * Crea un comentario a un feedback o una respuesta a otro comentario.
     * * @param feedbackId ID del feedback al que se le quiere comentar.
     * * @param request Contiene el contenido del comentario y opcionalmente el ID del comentario padre.
     */
    @Transactional
    public CommentResponseDto createComment(Integer feedbackId, CreateCommentRequestDto request, String username) {
        FeedbackProject feedbackProject = feedbackProjectRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback no encontrado con ID: " + feedbackId));
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Comment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("El comentario padre no fue encontrado con ID: " + request.getParentCommentId()));
        }

        Comment newComment = commentMapper.toEntity(request);
        newComment.setAuthor(author);
        newComment.setFeedbackProject(feedbackProject);
        newComment.setParentComment(parentComment);

        Comment savedComment = commentRepository.save(newComment);
        return commentMapper.toResponseDto(savedComment);
    }

    @Transactional
    public CommentResponseDto updateComment(Integer commentId, CreateCommentRequestDto request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con ID: " + commentId));

        // La autorización se delega a @PreAuthorize en el controlador.
        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toResponseDto(updatedComment);
    }

    @Transactional
    public void deleteComment(Integer commentId) {
        // La autorización se delega a @PreAuthorize en el controlador.
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comentario no encontrado con ID: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }


    /**
     * =================================
     * Autorizacion para springSecurity
     * Verifica si el usuario es el propietario del comentario.
     *
     * @param commentId ID del comentario a verificar.
     * @param username  Nombre de usuario del autor del comentario.
     *                  =================================
     */

    @Transactional(readOnly = true)
    public boolean isCommentOwner(Integer commentId, String username) {
        return commentRepository.findById(commentId)
                .map(comment -> comment.getAuthor().getUsername().equals(username))
                .orElse(false);
    }

}
