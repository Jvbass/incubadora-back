package com.incubadora.incubadora.dev.repository;

import com.incubadora.incubadora.dev.entity.common.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    /**
     * Busca todas las notificaciones para un usuario específico (destinatario),
     * ordenadas por fecha de creación para mostrar las más recientes primero.
     *
     * @param recipientId El ID del usuario destinatario.
     * @return Una lista de sus notificaciones.
     */
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Integer recipientId);

    /**
     * Cuenta la cantidad de notificaciones no leídas para un usuario específico.
     * Útil para mostrar el número en la campanita de notificaciones.
     *
     * @param recipientId El ID del usuario destinatario.
     * @return El número de notificaciones no leídas.
     */
    long countByRecipientIdAndIsReadFalse(Integer recipientId);
}
