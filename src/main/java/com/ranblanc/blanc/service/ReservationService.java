package com.ranblanc.blanc.service;


import com.ranblanc.blanc.dto.ReservationDTO;
import com.ranblanc.blanc.entity.Reservation;
import com.ranblanc.blanc.entity.Resource;
import com.ranblanc.blanc.entity.User;
import com.ranblanc.blanc.mapper.ReservationMapper;
import com.ranblanc.blanc.repository.ReservationRepository;
import com.ranblanc.blanc.repository.ResourceRepository;
import com.ranblanc.blanc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceRepository resourceRepository;

    // Sémaphore pour limiter l'accès concurrent (exemple : 5 accès simultanés)
    private final Semaphore semaphore = new Semaphore(5);

    // Synchronisation sur la classe pour éviter les conflits de réservation
    public synchronized ReservationDTO reserver(ReservationDTO dto) throws Exception {
        semaphore.acquire();
        try {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
            Resource resource = resourceRepository.findById(dto.getResourceId())
                    .orElseThrow(() -> new IllegalArgumentException("Ressource non trouvée"));

            // Vérification de conflit
            boolean conflit = reservationRepository.existsByResourceAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                    resource, dto.getDateFin(), dto.getDateDebut());
            if (conflit) {
                throw new IllegalStateException("Conflit de réservation détecté !");
            }

            Reservation reservation = ReservationMapper.toEntity(dto, user, resource);
            reservation.setStatut("ACTIVE");
            return ReservationMapper.toDTO(reservationRepository.save(reservation));
        } finally {
            semaphore.release();
        }
    }

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReservationDTO> getReservationById(Long id) {
        return reservationRepository.findById(id).map(ReservationMapper::toDTO);
    }

    public void annulerReservation(Long id) {
        reservationRepository.findById(id).ifPresent(reservation -> {
            reservation.setStatut("ANNULEE");
            reservationRepository.save(reservation);
        });
    }

    public List<ReservationDTO> getReservationsByUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();
        return reservationRepository.findByUser(user).stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> getReservationsByResource(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId).orElse(null);
        if (resource == null) return List.of();
        return reservationRepository.findByResource(resource).stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void cleanupExpiredReservations() {
        List<Reservation> expired = reservationRepository.findByDateFinBefore(LocalDateTime.now());
        for (Reservation r : expired) {
            r.setStatut("EXPIREE");
            reservationRepository.save(r);
        }
    }
} 