package com.ranblanc.blanc.mapper;


import com.ranblanc.blanc.dto.ResourceDTO;
import com.ranblanc.blanc.entity.Resource;

public class ResourceMapper {
    public static ResourceDTO toDTO(Resource resource) {
        if (resource == null) return null;
        ResourceDTO dto = new ResourceDTO();
        dto.setId(resource.getId());
        dto.setNom(resource.getNom());
        dto.setType(resource.getType());
        return dto;
    }

    public static Resource toEntity(ResourceDTO dto) {
        if (dto == null) return null;
        return Resource.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .type(dto.getType())
                .build();
    }
} 