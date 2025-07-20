package com.incubadora.incubadora.dev.service;

import com.incubadora.incubadora.dev.dto.CreateFeedbackProjectRequestDto;
import com.incubadora.incubadora.dev.dto.FeedbackResponseDto;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackProject;
import com.incubadora.incubadora.dev.entity.project.Project;
import com.incubadora.incubadora.dev.exception.OperationNotAllowedException;
import com.incubadora.incubadora.dev.exception.ResourceNotFoundException;
import com.incubadora.incubadora.dev.repository.FeedbackProjectRepository;
import com.incubadora.incubadora.dev.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class FeedbackService {
    private final FeedbackProjectRepository feedbackRepository;
    private final ProjectRepository projectRepository;

    public FeedbackService(FeedbackProjectRepository feedbackRepository, ProjectRepository projectRepository) {
        this.feedbackRepository = feedbackRepository;
        this.projectRepository = projectRepository;
    }


    /*
     * Crea un feedback para un proyecto.
     * Dueño del proyecto no puede dar feedback a su propio proyecto.
     * El proyecto debe estar publicado para poder dar feedback.
     * */
    @Transactional
    public FeedbackResponseDto createFeedback(String projectSlug, CreateFeedbackProjectRequestDto request, User currentUser) {
        // 1. Buscamos el proyecto por SLUG
        Project project = projectRepository.findBySlug(projectSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con slug: " + projectSlug));

        // limita feedback solo a proyectos publicados
        if (!"published".equalsIgnoreCase(project.getStatus())) {
            throw new OperationNotAllowedException("No se puede dar feedback a un proyecto que no está publicado.");
        }
        // Verificamos que el usuario no sea el dueño del proyecto
        if (Objects.equals(project.getDeveloper().getId(), currentUser.getId())) {
            throw new OperationNotAllowedException("No puedes dar feedback a tu propio proyecto.");
        }

        FeedbackProject feedback = new FeedbackProject(project, currentUser, request.getFeedbackDescription(), request.getRating());
        FeedbackProject savedFeedback = feedbackRepository.save(feedback);
        return mapToDto(savedFeedback);
    }


    // metodo para obtener los feedbacks de un proyecto
    @Transactional(readOnly = true)
    public List<FeedbackResponseDto> getFeedbackForProject(String projectSlug) {
        // 1. Buscamos el proyecto por SLUG
        Project project = projectRepository.findBySlug(projectSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con slug: " + projectSlug));

        // El resto de tu lógica original permanece intacta
        if (!"published".equalsIgnoreCase(project.getStatus())) {
            return Collections.emptyList();
        }

        // Usamos el ID del proyecto encontrado para buscar sus feedbacks
        return feedbackRepository.findByProjectId(project.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    // Editar feedback
    @Transactional
    public FeedbackResponseDto updateFeedback(Integer feedbackId, CreateFeedbackProjectRequestDto request, User currentUser) {
        FeedbackProject feedback = findFeedbackByIdAndCheckOwnership(feedbackId, currentUser.getId());

        if (!"published".equalsIgnoreCase(feedback.getProject().getStatus())) {
            throw new OperationNotAllowedException("No se puede editar el feedback porque el proyecto ya no está publicado.");
        }

        feedback.setFeedbackDescription(request.getFeedbackDescription());
        feedback.setRating(request.getRating());
        FeedbackProject updatedFeedback = feedbackRepository.save(feedback);
        return mapToDto(updatedFeedback);
    }


    // borrar feedback
    @Transactional
    public void deleteFeedback(Integer feedbackId, User currentUser) {
        FeedbackProject feedback = findFeedbackByIdAndCheckOwnership(feedbackId, currentUser.getId());

        if (!"published".equalsIgnoreCase(feedback.getProject().getStatus())) {
            throw new OperationNotAllowedException("No se puede borrar el feedback porque el proyecto ya no está publicado.");
        }

        feedbackRepository.delete(feedback);
    }


    // metodo para encontrar feedback por ID y verificar la propiedad
    private FeedbackProject findFeedbackByIdAndCheckOwnership(Integer feedbackId, Integer currentUserId) {
        FeedbackProject feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback no encontrado con ID: " + feedbackId));

        if (!Objects.equals(feedback.getAuthor().getId(), currentUserId)) {
            throw new OperationNotAllowedException("No tienes permiso para modificar este feedback.");
        }
        return feedback;
    }

    private FeedbackResponseDto mapToDto(FeedbackProject feedback) {
        FeedbackResponseDto dto = new FeedbackResponseDto();
        dto.setId(feedback.getId());
        dto.setFeedbackDescription(feedback.getFeedbackDescription());
        dto.setRating(feedback.getRating());
        dto.setAuthorId(feedback.getAuthor().getId());
        dto.setAuthor(feedback.getAuthor().getUsername());
        dto.setProjectId(feedback.getProject().getId());
        dto.setCreatedAt(feedback.getCreatedAt());
        dto.setUpdatedAt(feedback.getUpdatedAt());
        return dto;
    }
}
