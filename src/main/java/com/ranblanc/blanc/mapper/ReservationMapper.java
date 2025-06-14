package com.ranblanc.blanc.mapper;

import com.ranblanc.blanc.dto.ReservationDTO;
import com.ranblanc.blanc.entity.Reservation;
import com.ranblanc.blanc.entity.Resource;
import com.ranblanc.blanc.entity.User;


public class ReservationMapper {
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