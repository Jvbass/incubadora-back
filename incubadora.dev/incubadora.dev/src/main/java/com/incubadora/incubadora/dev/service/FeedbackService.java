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


// Crear feedback
    @Transactional
    public FeedbackResponseDto createFeedback(Integer projectId, CreateFeedbackProjectRequestDto request, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + projectId));

        if (!"published".equalsIgnoreCase(project.getStatus())) {
            throw new OperationNotAllowedException("No se puede dar feedback a un proyecto que no está publicado.");
        }

        if (Objects.equals(project.getDeveloper().getId(), currentUser.getId())) {
            throw new OperationNotAllowedException("No puedes dar feedback a tu propio proyecto.");
        }

        FeedbackProject feedback = new FeedbackProject(project, currentUser, request.getFeedbackDescription(), request.getRating());
        FeedbackProject savedFeedback = feedbackRepository.save(feedback);
        return mapToDto(savedFeedback);
    }



    // metodo para obtener los feedbacks de un proyecto
    @Transactional(readOnly = true)
    public List<FeedbackResponseDto> getFeedbackForProject(Integer projectId) {
        // Primero, verificamos que el proyecto exista.
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + projectId));

        // Regla de negocio: solo se muestra feedback de proyectos publicados.
        if (!"published".equalsIgnoreCase(project.getStatus())) {
            // Devolvemos una lista vacía si el proyecto no está publicado.
            // Es una mejor experiencia de usuario que devolver un error.
            return Collections.emptyList();
        }

        // Buscamos y mapeamos los feedbacks.
        return feedbackRepository.findByProjectId(projectId).stream()
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
        feedback.setRating(request.getRating()); // Actualizamos el rating
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



//helper para mapear FeedbackProject a FeedbackResponseDto
    private FeedbackResponseDto mapToDto(FeedbackProject feedback) {
        FeedbackResponseDto dto = new FeedbackResponseDto();
        dto.setId(feedback.getId());
        dto.setFeedbackDescription(feedback.getFeedbackDescription());
        dto.setRating(feedback.getRating()); // Mapeamos el rating
        dto.setAuthorId(feedback.getAuthor().getId());
        dto.setAuthor(feedback.getAuthor().getUsername());
        dto.setProjectId(feedback.getProject().getId());
        dto.setCreatedAt(feedback.getCreatedAt());
        dto.setUpdatedAt(feedback.getUpdatedAt());
        return dto;
    }

}
