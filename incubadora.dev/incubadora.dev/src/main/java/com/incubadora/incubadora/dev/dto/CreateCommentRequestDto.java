package com.incubadora.incubadora.dev.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateCommentRequestDto {
    @NotBlank(message = "El contenido del comentario no puede estar vacío.")
    private String content;

    // Opcional: El ID del comentario al que se está respondiendo.
    private Integer parentCommentId;


    // Getters y Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
