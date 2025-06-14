package com.ranblanc.blanc.repository;


import com.ranblanc.blanc.entity.Reservation;
import com.ranblanc.blanc.entity.Resource;
import com.ranblanc.blanc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour accéder aux données des réservations.
 * Fournit des méthodes pour rechercher, créer, mettre à jour et supprimer des réservations.
 */

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    /**
     * Trouve toutes les réservations d'un utilisateur.
     * 
     * @param user L'utilisateur dont on veut les réservations
     * @return La liste des réservations de l'utilisateur
     */
    List<Reservation> findByUser(User user);
    
    /**
     * Trouve toutes les réservations d'une ressource.
     * 
     * @param resource La ressource dont on veut les réservations
     * @return La liste des réservations de la ressource
     */
    List<Reservation> findByResource(Resource resource);
    
    /**
     * Trouve toutes les réservations avec un statut donné.
     * 
     * @param statut Le statut recherché
     * @return La liste des réservations avec ce statut
     */
    List<Reservation> findByStatut(String statut);
    
    /**
     * Trouve toutes les réservations dont la date de fin est antérieure à une date donnée.
     * Utilisé pour le nettoyage des réservations expirées.
     * 
     * @param dateTime La date de référence
     * @return La liste des réservations expirées
     */
    List<Reservation> findByDateFinBefore(LocalDateTime dateTime);
    
    /**
     * Vérifie s'il existe des réservations pour une ressource donnée qui chevauchent une période donnée.
     * Utilisé pour la vérification des conflits lors de la création d'une réservation.
     * 
     * @param resource La ressource à vérifier
     * @param fin La date de fin de la période
     * @param debut La date de début de la période
     * @return true s'il existe un conflit, false sinon
     */
    boolean existsByResourceAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(Resource resource, LocalDateTime fin, LocalDateTime debut);
    
    /**
     * Vérifie s'il existe des réservations pour une ressource donnée.
     * Utilisé pour vérifier si une ressource peut être supprimée.
     * 
     * @param resource La ressource à vérifier
     * @return true s'il existe des réservations pour cette ressource, false sinon
     */
    boolean existsByResource(Resource resource);
    long countByStatut(String statut);
}