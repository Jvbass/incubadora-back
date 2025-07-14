package com.incubadora.incubadora.dev.controller;

import com.incubadora.incubadora.dev.dto.CreateFeedbackProjectRequestDto;
import com.incubadora.incubadora.dev.dto.FeedbackResponseDto;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "6. Feedback de Proyectos", description = "Operaciones para dar, editar y borrar feedback en proyectos")
public class FeedbackProjectController {

    private final FeedbackService feedbackService;

    public FeedbackProjectController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /*===================================
     * Endpoint para crear un nuevo feedback para un proyecto.
     * Solo los usuarios autenticados pueden acceder a este endpoint y el usuario que creo el proyecto no puede crear feedback en su propio proyecto.
     *=================================== */
    @Operation(summary = "Crea un nuevo feedback para un proyecto")
    @PostMapping("/projects/{projectSlug}/feedback") // CAMBIO: de projectId a projectSlug
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FeedbackResponseDto> createFeedback(
            @PathVariable String projectSlug, // CAMBIO: de Integer a String
            @Valid @RequestBody CreateFeedbackProjectRequestDto request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        FeedbackResponseDto createdFeedback = feedbackService.createFeedback(projectSlug, request, currentUser);
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }


    /*===================================
     *  Actualiza un feedback existente.
     * ===================================*/
    // Los endpoints de editar y borrar no dependen del proyecto
    // Se identifican por el ID del propio feedback.
    @Operation(summary = "Actualiza un feedback existente")
    @PutMapping("/feedback/{feedbackId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FeedbackResponseDto> updateFeedback(
            @PathVariable Integer feedbackId,
            @Valid @RequestBody CreateFeedbackProjectRequestDto request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        FeedbackResponseDto updatedFeedback = feedbackService.updateFeedback(feedbackId, request, currentUser);
        return ResponseEntity.ok(updatedFeedback);
    }

    /*===================================
     * Elimina un feedback existente.
     * ==================================*/
    @Operation(summary = "Elimina un feedback existente")
    @DeleteMapping("/feedback/{feedbackId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable Integer feedbackId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        feedbackService.deleteFeedback(feedbackId, currentUser);
        return ResponseEntity.noContent().build();
    }

    /*===================================
     * Obtiene todos los feedbacks de un proyecto.
     *===================================*/
    @Operation(summary = "Obtiene todos los feedbacks de un proyecto")
    @GetMapping("/projects/{projectSlug}/feedback") // CAMBIO: de projectId a projectSlug
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackForProject(
            @PathVariable String projectSlug) { // CAMBIO: de Integer a String
        List<FeedbackResponseDto> feedbackList = feedbackService.getFeedbackForProject(projectSlug);
        return ResponseEntity.ok(feedbackList);
    }
}

