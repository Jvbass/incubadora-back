package com.incubadora.incubadora.dev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateFeedbackProjectRequestDto {
    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 500, message = "La descripcion no puede exceder los 500 caracteres.")
    private String feedbackDescription;

    @NotNull(message = "La evaluación es requerida.")
    private Byte rating;

    // Getters y Setters
    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }
}
