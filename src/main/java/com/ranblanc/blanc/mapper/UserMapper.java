package com.ranblanc.blanc.mapper;


import com.ranblanc.blanc.dto.UserDTO;
import com.ranblanc.blanc.entity.User;

/**
 * Classe utilitaire pour convertir entre les entités User et les DTOs UserDTO.
 * Cette classe fournit des méthodes statiques pour faciliter la conversion
 * bidirectionnelle entre les objets de domaine et les objets de transfert de données.
 */

public class UserMapper {
    
    /**
     * Convertit une entité User en DTO UserDTO.
     * Note: Le mot de passe n'est pas inclus dans le DTO pour des raisons de sécurité.
     * 
     * @param user L'entité User à convertir
     * @return Le DTO UserDTO correspondant, ou null si l'entité est null
     */
    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setEmail(user.getEmail());
        // Le mot de passe n'est pas copié pour des raisons de sécurité
        return dto;
    }

    /**
     * Convertit un DTO UserDTO en entité User.
     * Note: Cette méthode ne définit pas les rôles par défaut, cela doit être fait au niveau service.
     * 
     * @param dto Le DTO UserDTO à convertir
     * @return L'entité User correspondante, ou null si le DTO est null
     */
    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .email(dto.getEmail())
                .password(dto.getPassword()) // Ajout du mot de passe
                .build();
    }
    
    /**
     * Convertit un DTO UserDTO en entité User avec un rôle spécifié.
     * 
     * @param dto Le DTO UserDTO à convertir
     * @param role Le rôle à attribuer à l'utilisateur
     * @return L'entité User correspondante, ou null si le DTO est null
     */
    public static User toEntity(UserDTO dto, String role) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .roles(role)
                .build();
    }
}