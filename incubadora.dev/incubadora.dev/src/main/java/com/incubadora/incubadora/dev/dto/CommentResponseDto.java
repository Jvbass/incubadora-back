package com.incubadora.incubadora.dev.dto;

import java.sql.Timestamp;
import java.util.List;

public class CommentResponseDto {
    private Integer id;
    private String content;
    private AuthorDto author;
    private Timestamp createdAt;
    private List<CommentResponseDto> replies; // Lista de respuestas anidadas

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentResponseDto> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentResponseDto> replies) {
        this.replies = replies;
    }
}
