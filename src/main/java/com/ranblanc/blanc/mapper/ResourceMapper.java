package com.ranblanc.blanc.mapper;


import com.ranblanc.blanc.dto.ResourceDTO;
import com.ranblanc.blanc.entity.Resource;

/**
 * Classe utilitaire pour convertir entre les entités Resource et les DTOs ResourceDTO.
 * Cette classe fournit des méthodes statiques pour faciliter la conversion
 * bidirectionnelle entre les objets de domaine et les objets de transfert de données.
 */

public class ResourceMapper {
    
    /**
     * Convertit une entité Resource en DTO ResourceDTO.
     * 
     * @param resource L'entité Resource à convertir
     * @return Le DTO ResourceDTO correspondant, ou null si l'entité est null
     */
    public static ResourceDTO toDTO(Resource resource) {
        if (resource == null) return null;
        ResourceDTO dto = new ResourceDTO();
        dto.setId(resource.getId());
        dto.setNom(resource.getNom());
        dto.setType(resource.getType());
        return dto;
    }

    /**
     * Convertit un DTO ResourceDTO en entité Resource.
     * 
     * @param dto Le DTO ResourceDTO à convertir
     * @return L'entité Resource correspondante, ou null si le DTO est null
     */
    public static Resource toEntity(ResourceDTO dto) {
        if (dto == null) return null;
        return Resource.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .type(dto.getType())
                .build();
    }
}