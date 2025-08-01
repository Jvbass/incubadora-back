package com.incubadora.incubadora.dev.enums;

/**
 * Define los criterios de ordenamiento disponibles para la lista de proyectos.
 */
public enum ProjectSortBy {
    /**
     * Ordena por los proyectos más recientes.
     */

    LATEST,

    /**
     * Ordena por los proyectos con más feedbacks en los últimos 7 días.
     */
    MOST_FEEDBACK,

    /**
     * Ordena por los proyectos con mejor rating promedio en los últimos 7 días.
     */
    TOP_RATED
}