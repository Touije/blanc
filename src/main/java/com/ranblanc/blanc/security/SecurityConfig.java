package com.ranblanc.blanc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité pour l'application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Définit l'encodeur de mot de passe utilisé pour l'authentification.
     * 
     * @return L'encodeur de mot de passe configuré
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure le fournisseur d'authentification qui utilise le service de détails utilisateur
     * et l'encodeur de mot de passe.
     *
     * @return Le fournisseur d'authentification configuré
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     * Définit les règles d'autorisation pour les différentes URL de l'application.
     *
     * @param http La configuration de sécurité HTTP
     * @return La chaîne de filtres de sécurité configurée
     * @throws Exception Si une erreur survient lors de la configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers("/api/users/**", "/api/resources/**").hasRole("ADMIN")
                        .requestMatchers("/api/reservations/**").hasAnyRole("ADMIN", "CLIENT")
                        .anyRequest().authenticated()
                )
                .formLogin()
                .and()
                .httpBasic();
        
        return http.build();
    }
}