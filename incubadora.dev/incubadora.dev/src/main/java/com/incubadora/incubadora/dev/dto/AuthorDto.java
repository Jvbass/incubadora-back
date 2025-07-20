package com.incubadora.incubadora.dev.dto;

public class AuthorDto {
    private String username;

    public AuthorDto(String username) { this.username = username; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}