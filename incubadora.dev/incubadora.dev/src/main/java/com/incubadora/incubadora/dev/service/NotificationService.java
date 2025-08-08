package com.incubadora.incubadora.dev.service;

import com.incubadora.incubadora.dev.dto.NotificationResponseDto;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackProject;
import com.incubadora.incubadora.dev.entity.common.Notification;
import com.incubadora.incubadora.dev.exception.ResourceNotFoundException;
import com.incubadora.incubadora.dev.mapper.NotificationMapper;
import com.incubadora.incubadora.dev.repository.NotificationRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
        this.notificationRepository = notificationRepository;
    }

    // --- LÓGICA PARA CREAR NOTIFICACIONES ---
    @Transactional
    public void createNotificationForNewFeedback(FeedbackProject feedback) {
        User recipient = feedback.getProject().getDeveloper();
        User sender = feedback.getAuthor();

        if (recipient.getId().equals(sender.getId())) return;

        String message = String.format("%s ha dejado un feedback en tu proyecto '%s'", sender.getUsername(), feedback.getProject().getTitle());
        String link = String.format("/project/%s", feedback.getProject().getSlug());

        Notification notification = new Notification(recipient, sender, "NEW_FEEDBACK", message, link);
        notificationRepository.save(notification);
    }

    // --- LÓGICA PARA LEER Y GESTIONAR NOTIFICACIONES ---

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getNotificationsForUser(User user) {
        List<Notification> notifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(user.getId());
        return notificationMapper.toNotificationDtoList(notifications);
    }

    @Transactional
    public NotificationResponseDto markAsRead(Integer notificationId, User user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + notificationId));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new AccessDeniedException("No tienes permiso para modificar esta notificación.");
        }

        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationDto(updatedNotification);
    }

    @Transactional
    public List<NotificationResponseDto> markAllAsRead(User user) {
        List<Notification> unreadNotifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .filter(n -> !n.isRead())
                .collect(Collectors.toList());

        unreadNotifications.forEach(notification -> notification.setRead(true));
        List<Notification> updatedNotifications = notificationRepository.saveAll(unreadNotifications);

        return notificationMapper.toNotificationDtoList(updatedNotifications);
    }
}
