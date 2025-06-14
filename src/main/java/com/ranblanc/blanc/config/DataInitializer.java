package com.ranblanc.blanc.config;


import com.ranblanc.blanc.entity.User;
import com.ranblanc.blanc.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration pour initialiser les données de base dans l'application.
 * Cette classe crée des utilisateurs par défaut lors du démarrage de l'application
 * si ceux-ci n'existent pas déjà dans la base de données.
 */

@Configuration
public class DataInitializer {
    /**
     * Crée un CommandLineRunner qui initialise les utilisateurs par défaut.
     * 
     * @param userRepository Repository pour accéder aux utilisateurs
     * @param passwordEncoder Encodeur pour sécuriser les mots de passe
     * @return CommandLineRunner qui s'exécute au démarrage de l'application
     */
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Création d'un utilisateur administrateur si non existant
            if (userRepository.findByEmail("admin@ranblanc.com").isEmpty()) {
                userRepository.save(User.builder()
                        .nom("Administrateur")
                        .email("admin@ranblanc.com")
                        .password(passwordEncoder.encode("admin123"))
                        .roles("ADMIN")
                        .build());
            }
            
            // Création d'un utilisateur client si non existant
            if (userRepository.findByEmail("client@ranblanc.com").isEmpty()) {
                userRepository.save(User.builder()
                        .nom("Client")
                        .email("client@ranblanc.com")
                        .password(passwordEncoder.encode("client123"))
                        .roles("CLIENT")
                        .build());
            }
        };
    }
}
