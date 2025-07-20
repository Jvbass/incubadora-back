package com.incubadora.incubadora.dev.controller;

import com.incubadora.incubadora.dev.dto.CommentResponseDto;
import com.incubadora.incubadora.dev.dto.CreateCommentRequestDto;
import com.incubadora.incubadora.dev.service.CommentService;
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
@Tag(name = "Comentarios de Feedback", description = "Operaciones para gestionar los comentarios sobre un feedback")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Obtiene todos los comentarios de un feedback específico")
    @GetMapping("/feedback/{feedbackId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForFeedback(@PathVariable Integer feedbackId) {
        List<CommentResponseDto> comments = commentService.getCommentsForFeedback(feedbackId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Crea un nuevo comentario o una respuesta a otro comentario")
    @PostMapping("/feedback/{feedbackId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Integer feedbackId,
            @Valid @RequestBody CreateCommentRequestDto request,
            Authentication authentication) {
        String username = authentication.getName();
        CommentResponseDto createdComment = commentService.createComment(feedbackId, request, username);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualiza un comentario existente (solo el autor)")
    @PutMapping("/comments/{commentId}")
    @PreAuthorize("@commentService.isCommentOwner(#commentId, authentication.name)")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Integer commentId,
            @Valid @RequestBody CreateCommentRequestDto request) {
        CommentResponseDto updatedComment = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(updatedComment);
    }


    @Operation(summary = "Elimina un comentario existente (solo el autor)")
    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("@commentService.isCommentOwner(#commentId, authentication.name)")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
