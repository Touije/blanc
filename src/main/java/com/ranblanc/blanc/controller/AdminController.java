package com.ranblanc.blanc.controller;

import com.ranblanc.blanc.service.ReservationService;
import com.ranblanc.blanc.service.ResourceService;
import com.ranblanc.blanc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ResourceService resourceService;

    // Statistiques générales
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        long userCount = userService.getUserCount();
        long resourceCount = resourceService.getResourceCount();
        long activeReservationCount = reservationService.getReservationCountByStatus(ReservationService.STATUT_ACTIVE);

        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userCount);
        stats.put("resourceCount", resourceCount);
        stats.put("activeReservationCount", activeReservationCount);

        return ResponseEntity.ok(stats);
    }

    // Gestion des utilisateurs (promotion/rétrogradation)
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> roleMap) {
        String newRole = roleMap.get("role");
        if (newRole == null || (!newRole.equals("ADMIN") && !newRole.equals("CLIENT"))) {
            return ResponseEntity.badRequest().body("Rôle invalide. Utilisez 'ADMIN' ou 'CLIENT'");
        }

        boolean updated = userService.updateUserRole(id, newRole);
        if (updated) {
            return ResponseEntity.ok("Rôle mis à jour avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}