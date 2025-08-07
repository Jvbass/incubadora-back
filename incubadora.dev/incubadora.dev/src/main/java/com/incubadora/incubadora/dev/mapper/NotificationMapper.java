package com.incubadora.incubadora.dev.mapper;

import com.incubadora.incubadora.dev.dto.NotificationResponseDto;
import com.incubadora.incubadora.dev.entity.common.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "timeAgo", expression = "java(calculateTimeAgo(notification.getCreatedAt()))")
    NotificationResponseDto toNotificationDto(Notification notification);

    List<NotificationResponseDto> toNotificationDtoList(List<Notification> notifications);

    /**
     * Metodo jhecho para calcular el tiempo transcurrido desde una fecha pasada hasta ahora
     *
     * @param past La fecha pasada
     * @return Una cadena que representa el tiempo transcurrido en un formato legible
     */
    default String calculateTimeAgo(Timestamp past) {
        if (past == null) return "";
        Duration duration = Duration.between(past.toInstant(), Instant.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return "hace un momento";
        long minutes = seconds / 60;
        if (minutes < 60) return String.format("hace %d minuto%s", minutes, minutes > 1 ? "s" : "");
        long hours = minutes / 60;
        if (hours < 24) return String.format("hace %d hora%s", hours, hours > 1 ? "s" : "");
        long days = hours / 24;
        if (days < 30) return String.format("hace %d día%s", days, days > 1 ? "s" : "");
        long months = days / 30;
        if (months < 12) return String.format("hace %d mes%s", months, months > 1 ? "es" : "");
        long years = months / 12;
        return String.format("hace %d año%s", years, years > 1 ? "s" : "");
    }
}
