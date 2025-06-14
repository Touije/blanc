package com.ranblanc.blanc.service;


import com.ranblanc.blanc.dto.UserDTO;
import com.ranblanc.blanc.entity.User;
import com.ranblanc.blanc.mapper.UserMapper;
import com.ranblanc.blanc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour gérer les opérations liées aux utilisateurs.
 * Fournit des méthodes pour la création, la lecture, la mise à jour et la suppression d'utilisateurs.
 */


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Crée un nouvel utilisateur.
     * Note: Le mot de passe doit déjà être encodé avant d'appeler cette méthode.
     * 
     * @param userDTO Les données de l'utilisateur à créer
     * @return L'utilisateur créé avec son ID généré
     */
    public UserDTO createUser(UserDTO userDTO) {
        // Par défaut, si aucun rôle n'est spécifié, on attribue le rôle ADMIN
        // car cette méthode est utilisée par le contrôleur admin
        User user = UserMapper.toEntity(userDTO);
        if (user.getRoles() == null) {
            user.setRoles("ADMIN");
        }
        return UserMapper.toDTO(userRepository.save(user));
    }

    /**
     * Récupère tous les utilisateurs.
     * 
     * @return La liste de tous les utilisateurs
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un utilisateur par son ID.
     * 
     * @param id L'ID de l'utilisateur à récupérer
     * @return L'utilisateur correspondant ou empty si non trouvé
     */
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserMapper::toDTO);
    }

    /**
     * Récupère un utilisateur par son email.
     * 
     * @param email L'email de l'utilisateur à récupérer
     * @return L'utilisateur correspondant ou empty si non trouvé
     */
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(UserMapper::toDTO);
    }

    /**
     * Supprime un utilisateur par son ID.
     * 
     * @param id L'ID de l'utilisateur à supprimer
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Sauvegarde un utilisateur.
     * Utilisé principalement pour l'inscription des nouveaux utilisateurs.
     * 
     * @param user L'utilisateur à sauvegarder
     * @return L'utilisateur sauvegardé avec son ID généré
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }


    public long getUserCount() {
        return userRepository.count();
    }

    public boolean updateUserRole(Long id, String role) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRoles(role);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}