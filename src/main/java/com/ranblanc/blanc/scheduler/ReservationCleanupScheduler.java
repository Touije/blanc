package com.ranblanc.blanc.scheduler;


import com.ranblanc.blanc.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationCleanupScheduler {

    @Autowired
    private ReservationService reservationService;

    // Nettoyage toutes les heures (cron = "0 0 * * * *")
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredReservations() {
        reservationService.cleanupExpiredReservations();
    }
} 