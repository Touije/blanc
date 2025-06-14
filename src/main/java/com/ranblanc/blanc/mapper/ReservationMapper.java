package com.ranblanc.blanc.mapper;

import com.ranblanc.blanc.dto.ReservationDTO;
import com.ranblanc.blanc.entity.Reservation;
import com.ranblanc.blanc.entity.Resource;
import com.ranblanc.blanc.entity.User;

/**
 * Classe utilitaire pour convertir entre les entités Reservation et les DTOs ReservationDTO.
 * Cette classe fournit des méthodes statiques pour faciliter la conversion
 * bidirectionnelle entre les objets de domaine et les objets de transfert de données.
 */


public class ReservationMapper {
    
    /**
     * Convertit une entité Reservation en DTO ReservationDTO.
     * Les IDs des entités associées (User, Resource) sont extraits et inclus dans le DTO.
     * 
     * @param reservation L'entité Reservation à convertir
     * @return Le DTO ReservationDTO correspondant, ou null si l'entité est null
     */
    public static ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) return null;
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setResourceId(reservation.getResource().getId());
        dto.setDateDebut(reservation.getDateDebut());
        dto.setDateFin(reservation.getDateFin());
        dto.setStatut(reservation.getStatut());
        return dto;
    }

    /**
     * Convertit un DTO ReservationDTO en entité Reservation.
     * Nécessite les entités User et Resource correspondantes qui doivent être fournies.
     * 
     * @param dto Le DTO ReservationDTO à convertir
     * @param user L'entité User associée à cette réservation
     * @param resource L'entité Resource associée à cette réservation
     * @return L'entité Reservation correspondante, ou null si le DTO est null
     */
    public static Reservation toEntity(ReservationDTO dto, User user, Resource resource) {
        if (dto == null) return null;
        return Reservation.builder()
                .id(dto.getId())
                .user(user)
                .resource(resource)
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .statut(dto.getStatut())
                .build();
    }
}