package com.incubadora.incubadora.dev.controller;

import com.incubadora.incubadora.dev.dto.NotificationResponseDto;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notificaciones", description = "Operaciones para gestionar las notificaciones del usuario")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Obtiene todas las notificaciones del usuario autenticado")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getNotificationsForUser(currentUser));
    }

    @Operation(summary = "Marca una notificación específica como leída")
    @PatchMapping("/{notificationId}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationResponseDto> markNotificationAsRead(@PathVariable Integer notificationId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.markAsRead(notificationId, currentUser));
    }

    @Operation(summary = "Marca todas las notificaciones del usuario como leídas")
    @PostMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationResponseDto>> markAllNotificationsAsRead(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.markAllAsRead(currentUser));
    }
}
