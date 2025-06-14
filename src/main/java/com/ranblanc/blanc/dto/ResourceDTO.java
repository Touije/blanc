package com.ranblanc.blanc.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ResourceDTO {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le type est obligatoire")
    private String type;
    
    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères")
    private String description;
    
    private Boolean disponible = true;
    
    private String localisation;
}