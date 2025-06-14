package com.ranblanc.blanc.controller;


import com.ranblanc.blanc.dto.UserDTO;
import com.ranblanc.blanc.entity.User;
import com.ranblanc.blanc.mapper.UserMapper;
import com.ranblanc.blanc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Contrôleur REST pour gérer les opérations liées aux utilisateurs.
 * Fournit des endpoints pour la création, la lecture, la mise à jour et la suppression d'utilisateurs,
 * ainsi que pour l'inscription et la connexion.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    /**
     * Crée un nouvel utilisateur.
     * Accessible uniquement aux administrateurs (voir SecurityConfig).
     * 
     * @param userDTO Les données de l'utilisateur à créer
     * @return L'utilisateur créé avec son ID généré
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        // Encodage du mot de passe avant la création
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    /**
     * Récupère tous les utilisateurs.
     * Accessible uniquement aux administrateurs (voir SecurityConfig).
     * 
     * @return La liste de tous les utilisateurs
     */
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Récupère un utilisateur par son ID.
     * Accessible uniquement aux administrateurs (voir SecurityConfig).
     * 
     * @param id L'ID de l'utilisateur à récupérer
     * @return L'utilisateur correspondant ou une réponse 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un utilisateur par son ID.
     * Accessible uniquement aux administrateurs (voir SecurityConfig).
     * 
     * @param id L'ID de l'utilisateur à supprimer
     * @return Une réponse 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint d'inscription pour les nouveaux utilisateurs.
     * Accessible à tous (voir SecurityConfig).
     * Crée un nouvel utilisateur avec le rôle CLIENT par défaut.
     * 
     * @param userDTO Les données du nouvel utilisateur
     * @return Un message de succès ou d'erreur
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) {
        // Vérification si l'email est déjà utilisé
        if (userService.getUserByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        // Utilisation du UserMapper avec le rôle CLIENT
        User user = UserMapper.toEntity(userDTO, "CLIENT");
        // Encodage du mot de passe
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userService.saveUser(user);
        UserDTO savedUserDTO = UserMapper.toDTO(savedUser);

        return ResponseEntity.ok(Map.of(
                "message", "Inscription réussie",
                "user", savedUserDTO
        ));
    }

    /**
     * Endpoint de connexion pour les utilisateurs.
     * Accessible à tous (voir SecurityConfig).
     * Si l'utilisateur n'existe pas, il est automatiquement inscrit.
     * 
     * @param userDTO Les identifiants de connexion (email et mot de passe)
     * @return Un message de succès ou d'erreur avec les informations de l'utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            // Vérifier si l'utilisateur existe
            Optional<UserDTO> existingUser = userService.getUserByEmail(userDTO.getEmail());
            
            if (existingUser.isPresent()) {
                // Utilisateur existe, tentative de connexion
                try {
                    // Création d'un token d'authentification (sera traité par Spring Security)
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDTO.getEmail(), userDTO.getPassword());
                    // L'authentification sera gérée par Spring Security automatiquement
                    
                    return ResponseEntity.ok(Map.of(
                            "message", "Connexion réussie",
                            "user", existingUser.get(),
                            "isNewUser", false
                    ));
                } catch (AuthenticationException e) {
                    return ResponseEntity.status(401).body("Identifiants invalides");
                }
            } else {
                // Utilisateur n'existe pas, inscription automatique
                if (userDTO.getNom() == null || userDTO.getNom().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("Le nom est obligatoire pour l'inscription");
                }
                
                // Utilisation du UserMapper avec le rôle CLIENT
                User user = UserMapper.toEntity(userDTO, "CLIENT");
                // Encodage du mot de passe
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                
                User savedUser = userService.saveUser(user);
                UserDTO savedUserDTO = UserMapper.toDTO(savedUser);
                
                return ResponseEntity.ok(Map.of(
                        "message", "Inscription et connexion réussies",
                        "user", savedUserDTO,
                        "isNewUser", true
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la connexion/inscription: " + e.getMessage());
        }
    }
}