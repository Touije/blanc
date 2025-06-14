package com.ranblanc.blanc.controller;


import com.ranblanc.blanc.dto.ReservationDTO;
import com.ranblanc.blanc.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les opérations liées aux réservations.
 * Fournit des endpoints pour créer, lire, annuler et rechercher des réservations.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    /**
     * Service de gestion des réservations.
     */
    @Autowired
    private ReservationService reservationService;

    /**
     * Crée une nouvelle réservation.
     * 
     * @param reservationDTO Les données de la réservation à créer
     * @return La réservation créée ou un message d'erreur
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<?> reserver(@Valid @RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO created = reservationService.reserver(reservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            // Erreur de validation (utilisateur ou ressource non trouvée)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            // Conflit de réservation
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Autres erreurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création de la réservation: " + e.getMessage());
        }
    }

    /**
     * Récupère toutes les réservations.
     * Accessible uniquement par les administrateurs.
     * 
     * @return La liste de toutes les réservations
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    /**
     * Récupère une réservation par son ID.
     * 
     * @param id L'ID de la réservation à récupérer
     * @return La réservation correspondante ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Annule une réservation existante.
     * 
     * @param id L'ID de la réservation à annuler
     * @return 204 No Content si l'annulation est réussie
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Void> annulerReservation(@PathVariable Long id) {
        // Vérifie d'abord si la réservation existe
        if (reservationService.getReservationById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        reservationService.annulerReservation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère toutes les réservations d'un utilisateur.
     * 
     * @param userId L'ID de l'utilisateur
     * @return La liste des réservations de l'utilisateur
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENT') and #userId == authentication.principal.id)")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUser(@PathVariable Long userId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByUser(userId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Récupère toutes les réservations d'une ressource.
     * 
     * @param resourceId L'ID de la ressource
     * @return La liste des réservations de la ressource
     */
    @GetMapping("/resource/{resourceId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getReservationsByResource(@PathVariable Long resourceId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByResource(resourceId);
        return ResponseEntity.ok(reservations);
    }
}