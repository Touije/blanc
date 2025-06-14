package com.ranblanc.blanc.mapper;


import com.ranblanc.blanc.dto.UserDTO;
import com.ranblanc.blanc.entity.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .email(dto.getEmail())
                .build();
    }
} 