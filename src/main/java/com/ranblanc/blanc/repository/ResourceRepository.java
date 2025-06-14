package com.ranblanc.blanc.repository;


import com.ranblanc.blanc.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // Méthodes personnalisées si besoin
} 