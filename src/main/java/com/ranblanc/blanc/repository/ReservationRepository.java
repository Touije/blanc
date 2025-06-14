package com.ranblanc.blanc.repository;


import com.ranblanc.blanc.entity.Reservation;
import com.ranblanc.blanc.entity.Resource;
import com.ranblanc.blanc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByResource(Resource resource);
    List<Reservation> findByStatut(String statut);
    List<Reservation> findByDateFinBefore(LocalDateTime dateTime);
    boolean existsByResourceAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(Resource resource, LocalDateTime fin, LocalDateTime debut);
} 