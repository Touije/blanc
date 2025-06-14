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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * Service pour gérer les opérations liées aux réservations.
 * Implémente des mécanismes de synchronisation pour éviter les conflits de réservation
 * lors d'accès concurrents.
 */

@Service
public class ReservationService {
    
    /**
     * Constantes pour les statuts de réservation.
     * Utilisées pour éviter les erreurs de frappe et faciliter la maintenance.
     */
    public static final String STATUT_ACTIVE = "ACTIVE";
    public static final String STATUT_ANNULEE = "ANNULEE";
    public static final String STATUT_EXPIREE = "EXPIREE";

    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * Sémaphore pour limiter l'accès concurrent.
     * Permet de contrôler le nombre d'accès simultanés à la section critique (5 maximum).
     */
    private final Semaphore semaphore = new Semaphore(5);

    /**
     * Crée une nouvelle réservation en vérifiant qu'il n'y a pas de conflit.
     * Utilise une double protection contre les accès concurrents :
     * 1. synchronized pour garantir l'exclusion mutuelle au niveau de la méthode
     * 2. semaphore pour limiter le nombre d'accès simultanés
     *
     * @param dto Les données de la réservation à créer
     * @return La réservation créée avec son ID généré
     * @throws Exception Si une erreur survient pendant l'acquisition du sémaphore
     * @throws IllegalArgumentException Si l'utilisateur ou la ressource n'existe pas
     * @throws IllegalStateException Si un conflit de réservation est détecté
     */
    @Transactional
    public synchronized ReservationDTO reserver(ReservationDTO dto) throws Exception {
        // Acquisition du sémaphore (bloque si le nombre maximum d'accès est atteint)
        semaphore.acquire();
        try {
            // Récupération de l'utilisateur et de la ressource
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
            Resource resource = resourceRepository.findById(dto.getResourceId())
                    .orElseThrow(() -> new IllegalArgumentException("Ressource non trouvée"));

            // Vérification qu'il n'y a pas de conflit de réservation
            // Une réservation est en conflit si elle concerne la même ressource et que les périodes se chevauchent
            boolean conflit = reservationRepository.existsByResourceAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                    resource, dto.getDateFin(), dto.getDateDebut());
            if (conflit) {
                throw new IllegalStateException("Conflit de réservation détecté !");
            }

            // Création et sauvegarde de la réservation
            Reservation reservation = ReservationMapper.toEntity(dto, user, resource);
            reservation.setStatut(STATUT_ACTIVE);
            return ReservationMapper.toDTO(reservationRepository.save(reservation));
        } finally {
            // Libération du sémaphore (important pour éviter les deadlocks)
            semaphore.release();
        }
    }

    /**
     * Récupère toutes les réservations.
     * 
     * @return La liste de toutes les réservations
     */
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une réservation par son ID.
     * 
     * @param id L'ID de la réservation à récupérer
     * @return La réservation correspondante ou empty si non trouvée
     */
    public Optional<ReservationDTO> getReservationById(Long id) {
        return reservationRepository.findById(id).map(ReservationMapper::toDTO);
    }

    /**
     * Annule une réservation en changeant son statut.
     * 
     * @param id L'ID de la réservation à annuler
     */
    @Transactional
    public void annulerReservation(Long id) {
        reservationRepository.findById(id).ifPresent(reservation -> {
            reservation.setStatut(STATUT_ANNULEE);
            reservationRepository.save(reservation);
        });
    }

    /**
     * Récupère toutes les réservations d'un utilisateur.
     * 
     * @param userId L'ID de l'utilisateur
     * @return La liste des réservations de l'utilisateur
     */
    public List<ReservationDTO> getReservationsByUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of(); // Retourne une liste vide si l'utilisateur n'existe pas
        return reservationRepository.findByUser(user).stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les réservations d'une ressource.
     * 
     * @param resourceId L'ID de la ressource
     * @return La liste des réservations de la ressource
     */
    public List<ReservationDTO> getReservationsByResource(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId).orElse(null);
        if (resource == null) return List.of(); // Retourne une liste vide si la ressource n'existe pas
        return reservationRepository.findByResource(resource).stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Nettoie les réservations expirées en changeant leur statut.
     * Cette méthode est appelée périodiquement par le scheduler.
     */
    @Transactional
    public void cleanupExpiredReservations() {
        // Récupère toutes les réservations dont la date de fin est passée
        List<Reservation> expired = reservationRepository.findByDateFinBefore(LocalDateTime.now());
        
        // Change le statut de chaque réservation expirée
        for (Reservation r : expired) {
            // Ne change le statut que si la réservation est encore active
            if (STATUT_ACTIVE.equals(r.getStatut())) {
                r.setStatut(STATUT_EXPIREE);
                reservationRepository.save(r);
            }
        }
    }

    public long getReservationCountByStatus(String status) {
        return reservationRepository.countByStatut(status);
    }
}