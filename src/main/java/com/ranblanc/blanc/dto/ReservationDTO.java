package com.ranblanc.blanc.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long resourceId;

    @NotNull
    private LocalDateTime dateDebut;

    @NotNull
    private LocalDateTime dateFin;

    private String statut;
} 