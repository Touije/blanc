package com.ranblanc.blanc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour l'application.
 * Intercepte les exceptions lancées par les contrôleurs et les convertit en réponses HTTP appropriées.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Structure de réponse d'erreur standardisée.
     */
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private Map<String, String> validationErrors;
        
        public ErrorResponse(HttpStatus status, String message, String path) {
            this.timestamp = LocalDateTime.now();
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.message = message;
            this.path = path;
        }
        
        public ErrorResponse(HttpStatus status, String message, String path, Map<String, String> validationErrors) {
            this(status, message, path);
            this.validationErrors = validationErrors;
        }

        // Getters et setters
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public Map<String, String> getValidationErrors() { return validationErrors; }
        public void setValidationErrors(Map<String, String> validationErrors) { this.validationErrors = validationErrors; }
    }

    /**
     * Gère les exceptions de validation des arguments de méthode.
     * Se produit lorsque les validations @Valid échouent.
     * 
     * @param ex L'exception de validation
     * @param request La requête web
     * @return Une réponse avec les erreurs de validation et le statut 400 BAD REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST, 
                "Erreur de validation", 
                request.getDescription(false).replace("uri=", ""), 
                errors);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions d'argument illégal.
     * Se produit généralement lorsqu'une entité n'est pas trouvée.
     * 
     * @param ex L'exception d'argument illégal
     * @param request La requête web
     * @return Une réponse avec le message d'erreur et le statut 400 BAD REQUEST
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions d'état illégal.
     * Se produit généralement lors de conflits de réservation.
     * 
     * @param ex L'exception d'état illégal
     * @param request La requête web
     * @return Une réponse avec le message d'erreur et le statut 409 CONFLICT
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Gère les exceptions d'accès refusé.
     * Se produit lorsqu'un utilisateur tente d'accéder à une ressource sans les autorisations nécessaires.
     * 
     * @param ex L'exception d'accès refusé
     * @param request La requête web
     * @return Une réponse avec le message d'erreur et le statut 403 FORBIDDEN
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN, 
                "Accès refusé: vous n'avez pas les droits nécessaires pour accéder à cette ressource", 
                request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    
    /**
     * Gère les exceptions d'authentification.
     * Se produit lorsque les identifiants de connexion sont incorrects.
     * 
     * @param ex L'exception d'authentification
     * @param request La requête web
     * @return Une réponse avec le message d'erreur et le statut 401 UNAUTHORIZED
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED, 
                "Identifiants incorrects", 
                request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Gère toutes les autres exceptions non traitées.
     * 
     * @param ex L'exception
     * @param request La requête web
     * @return Une réponse avec le message d'erreur et le statut 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(
            Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erreur interne du serveur: " + ex.getMessage(), 
                request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}