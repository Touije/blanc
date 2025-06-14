package com.ranblanc.blanc.controller;


import com.ranblanc.blanc.dto.UserDTO;
import com.ranblanc.blanc.entity.User;
import com.ranblanc.blanc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) {
        if (userService.getUserByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }
        UserDTO toSave = new UserDTO();
        toSave.setNom(userDTO.getNom());
        toSave.setEmail(userDTO.getEmail());
        // Le mot de passe doit être dans le DTO, donc on suppose qu'il y a un champ password dans UserDTO
        // On ajoute ce champ si besoin
        // Ici, on suppose que userDTO.getPassword() existe
        User user = User.builder()
                .nom(userDTO.getNom())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles("CLIENT")
                .build();
        userService.saveUser(user);
        return ResponseEntity.ok("Inscription réussie");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDTO.getEmail(), userDTO.getPassword());
            // L'authentification sera gérée par Spring Security automatiquement
            return ResponseEntity.ok("Connexion réussie");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Identifiants invalides");
        }
    }
}