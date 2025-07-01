package com.incubadora.incubadora.dev.dto;

/**
 * DTO simple para representar una tecnología.
 */
public class TechnologyDto {
    private Integer id;
    private String name;
    private String techColor;

    // Constructor, Getters y Setters
    public TechnologyDto(Integer id, String name, String techColor) {
        this.id = id;
        this.name = name;
        this.techColor = techColor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechColor() {
        return techColor;
    }

    public void setTechColor(String techColor) {
        this.techColor = techColor;
    }
}

