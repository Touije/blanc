package com.ranblanc.blanc.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ResourceDTO {
    private Long id;

    @NotBlank
    private String nom;

    @NotBlank
    private String type;
} 