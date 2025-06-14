package com.ranblanc.blanc.config;


import com.ranblanc.blanc.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@ranblanc.com").isEmpty()) {
                userRepository.save(User.builder()
                        .username("Administrateur")
                        .email("admin@ranblanc.com")
                        .password(passwordEncoder.encode("admin123"))
                        .roles("ADMIN")
                        .build());
            }
            if (userRepository.findByEmail("client@ranblanc.com").isEmpty()) {
                userRepository.save(User.builder()
                        .username("Client")
                        .email("client@ranblanc.com")
                        .password(passwordEncoder.encode("client123"))
                        .roles("CLIENT")
                        .build());
            }
        };
    }
}
