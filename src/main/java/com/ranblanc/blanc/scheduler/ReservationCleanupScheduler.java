package com.ranblanc.blanc.scheduler;


import com.ranblanc.blanc.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Scheduler pour nettoyer automatiquement les réservations expirées.
 * Exécute périodiquement une tâche de nettoyage pour mettre à jour le statut
 * des réservations dont la date de fin est passée.
 */
@Component
public class ReservationCleanupScheduler {

    /**
     * Logger pour tracer l'exécution du scheduler.
     */
    private static final Logger logger = LoggerFactory.getLogger(ReservationCleanupScheduler.class);
    
    /**
     * Service de gestion des réservations.
     */
    @Autowired
    private ReservationService reservationService;

    /**
     * Nettoie les réservations expirées toutes les heures.
     * Utilise l'expression cron "0 0 * * * *" qui signifie :
     * - 0 seconde
     * - 0 minute
     * - Toutes les heures
     * - Tous les jours du mois
     * - Tous les mois
     * - Tous les jours de la semaine
     */
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        logger.info("Début du nettoyage des réservations expirées à {}", formattedDate);
        
        try {
            reservationService.cleanupExpiredReservations();
            logger.info("Nettoyage des réservations expirées terminé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors du nettoyage des réservations expirées: {}", e.getMessage(), e);
        }
    }
}