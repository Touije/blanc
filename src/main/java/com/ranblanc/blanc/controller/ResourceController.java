package com.ranblanc.blanc.controller;


import com.ranblanc.blanc.dto.ResourceDTO;
import com.ranblanc.blanc.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Contrôleur REST pour gérer les opérations liées aux ressources.
 * Fournit des endpoints pour créer, lire, mettre à jour et supprimer des ressources.
 */
@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    /**
     * Service de gestion des ressources.
     */
    @Autowired
    private ResourceService resourceService;

    /**
     * Crée une nouvelle ressource.
     * Accessible uniquement par les administrateurs.
     * 
     * @param resourceDTO Les données de la ressource à créer
     * @return La ressource créée avec son ID généré
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResourceDTO> createResource(@Valid @RequestBody ResourceDTO resourceDTO) {
        ResourceDTO created = resourceService.createResource(resourceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Récupère toutes les ressources.
     * Accessible par tous les utilisateurs authentifiés.
     * 
     * @return La liste de toutes les ressources
     */
    @GetMapping
    public ResponseEntity<List<ResourceDTO>> getAllResources() {
        List<ResourceDTO> resources = resourceService.getAllResources();
        return ResponseEntity.ok(resources);
    }

    /**
     * Récupère une ressource par son ID.
     * Accessible par tous les utilisateurs authentifiés.
     * 
     * @param id L'ID de la ressource à récupérer
     * @return La ressource correspondante ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResourceDTO> getResourceById(@PathVariable Long id) {
        return resourceService.getResourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une ressource par son ID.
     * Accessible uniquement par les administrateurs.
     * Vérifie d'abord si la ressource existe et si elle n'a pas de réservations associées.
     * 
     * @param id L'ID de la ressource à supprimer
     * @return 204 No Content si la suppression est réussie, 404 si la ressource n'existe pas,
     *         409 Conflict si la ressource a des réservations associées
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteResource(@PathVariable Long id) {
        try {
            resourceService.deleteResource(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Ressource non trouvée
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            // Ressource a des réservations associées
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}