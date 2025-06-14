package com.ranblanc.blanc.service;


import com.ranblanc.blanc.dto.ResourceDTO;
import com.ranblanc.blanc.entity.Resource;
import com.ranblanc.blanc.mapper.ResourceMapper;
import com.ranblanc.blanc.repository.ReservationRepository;
import com.ranblanc.blanc.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour gérer les opérations liées aux ressources.
 * Fournit des méthodes pour créer, lire, mettre à jour et supprimer des ressources.
 */
@Service
public class ResourceService {

    /**
     * Repository pour accéder aux données des ressources.
     */
    @Autowired
    private ResourceRepository resourceRepository;
    
    /**
     * Repository pour vérifier les réservations associées à une ressource.
     */
    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Crée une nouvelle ressource.
     * 
     * @param resourceDTO Les données de la ressource à créer
     * @return La ressource créée avec son ID généré
     */
    @Transactional
    public ResourceDTO createResource(ResourceDTO resourceDTO) {
        // Conversion du DTO en entité
        Resource resource = ResourceMapper.toEntity(resourceDTO);
        
        // Sauvegarde de la ressource et conversion du résultat en DTO
        return ResourceMapper.toDTO(resourceRepository.save(resource));
    }

    /**
     * Récupère toutes les ressources.
     * 
     * @return La liste de toutes les ressources
     */
    public List<ResourceDTO> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(ResourceMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une ressource par son ID.
     * 
     * @param id L'ID de la ressource à récupérer
     * @return La ressource correspondante ou empty si non trouvée
     */
    public Optional<ResourceDTO> getResourceById(Long id) {
        return resourceRepository.findById(id).map(ResourceMapper::toDTO);
    }

    /**
     * Supprime une ressource par son ID.
     * Vérifie d'abord si la ressource existe et si elle n'a pas de réservations associées.
     * 
     * @param id L'ID de la ressource à supprimer
     * @throws IllegalArgumentException Si la ressource n'existe pas
     * @throws IllegalStateException Si la ressource a des réservations associées
     */
    @Transactional
    public void deleteResource(Long id) {
        // Vérifie si la ressource existe
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ressource non trouvée avec l'ID: " + id));
        
        // Vérifie si la ressource a des réservations associées
        if (reservationRepository.existsByResource(resource)) {
            throw new IllegalStateException("Impossible de supprimer la ressource car elle a des réservations associées");
        }
        
        // Supprime la ressource
        resourceRepository.deleteById(id);
    }

    public long getResourceCount() {
        return resourceRepository.count();
    }
}