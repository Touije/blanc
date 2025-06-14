# RanBlanc – Système de Réservation de Ressources

## Présentation du projet

RanBlanc est une application complète de gestion de réservation de ressources développée avec Spring Boot. Elle permet aux administrateurs de gérer des ressources (salles, équipements, véhicules, etc.) et aux clients de réserver ces ressources pour des périodes spécifiques, tout en évitant les conflits de réservation.

Cette application implémente une architecture REST moderne avec une séparation claire des responsabilités, une gestion robuste de la sécurité et des mécanismes avancés pour garantir l'intégrité des données.

## Fonctionnalités détaillées

### Gestion des utilisateurs
- **Système de rôles** : Distinction entre administrateurs (ADMIN) et clients (CLIENT)
- **Inscription** : Les nouveaux utilisateurs peuvent s'inscrire et reçoivent automatiquement le rôle CLIENT
- **Authentification sécurisée** : Utilisation de Spring Security avec formulaire de connexion et authentification HTTP Basic
- **Gestion des profils** : Les administrateurs peuvent créer, consulter et supprimer des utilisateurs

### Gestion des ressources
- **Catalogue de ressources** : Les administrateurs peuvent créer et gérer différents types de ressources
- **Consultation** : Tous les utilisateurs authentifiés peuvent consulter les ressources disponibles
- **Suppression sécurisée** : Vérification de l'absence de réservations avant suppression

### Système de réservation
- **Réservation de ressources** : Les clients peuvent réserver des ressources pour des périodes spécifiques
- **Prévention des conflits** : Mécanisme avancé de détection et prévention des chevauchements de réservation
- **Gestion de la concurrence** : Utilisation de verrous et sémaphores pour éviter les conflits lors d'accès simultanés
- **Cycle de vie des réservations** : Suivi des statuts (ACTIVE, ANNULEE, EXPIREE)
- **Nettoyage automatique** : Tâche planifiée pour marquer automatiquement les réservations expirées

### Autres fonctionnalités
- **Documentation API** : Interface Swagger/OpenAPI pour explorer et tester l'API
- **Validation des données** : Contrôles de validation sur toutes les entrées utilisateur
- **Gestion globale des exceptions** : Traitement unifié des erreurs avec réponses HTTP appropriées
- **Journalisation** : Suivi des opérations importantes pour le débogage et l'audit

## Architecture et technologies

### Stack technologique

- **Backend** : Java 17 avec Spring Boot 3.5.0
- **Persistance** : Spring Data JPA avec Hibernate et MySQL
- **Sécurité** : Spring Security pour l'authentification et l'autorisation
- **Validation** : Jakarta Bean Validation (anciennement Hibernate Validator)
- **Documentation API** : Springdoc OpenAPI 2.8.8 (compatible Swagger UI)
- **Utilitaires** : 
  - Lombok pour la réduction du code boilerplate
  - Spring Boot DevTools pour le développement rapide

### Architecture logicielle

L'application suit une architecture en couches classique avec séparation des responsabilités :

```
src/main/java/com/ranblanc/blanc/
├── config/           # Configuration (Swagger, etc.)
│   └── SwaggerConfig.java       # Configuration de la documentation API
├── controller/       # Contrôleurs REST (API endpoints)
│   ├── UserController.java      # Gestion des utilisateurs et authentification
│   ├── ResourceController.java  # Gestion des ressources
│   └── ReservationController.java # Gestion des réservations
├── dto/              # Objets de transfert de données
│   ├── UserDTO.java            # DTO pour les utilisateurs
│   ├── ResourceDTO.java        # DTO pour les ressources
│   └── ReservationDTO.java     # DTO pour les réservations
├── entity/           # Entités JPA (modèle de données)
│   ├── User.java              # Entité utilisateur
│   ├── Resource.java          # Entité ressource
│   └── Reservation.java       # Entité réservation
├── exception/        # Gestion des exceptions
│   └── GlobalExceptionHandler.java # Gestionnaire global des exceptions
├── mapper/           # Convertisseurs entité <-> DTO
│   ├── UserMapper.java        # Conversion User <-> UserDTO
│   ├── ResourceMapper.java    # Conversion Resource <-> ResourceDTO
│   └── ReservationMapper.java # Conversion Reservation <-> ReservationDTO
├── repository/       # Repositories JPA (accès aux données)
│   ├── UserRepository.java      # Accès aux données utilisateurs
│   ├── ResourceRepository.java  # Accès aux données ressources
│   └── ReservationRepository.java # Accès aux données réservations
├── scheduler/        # Tâches planifiées
│   └── ReservationCleanupScheduler.java # Nettoyage des réservations expirées
├── security/         # Configuration de sécurité
│   ├── SecurityConfig.java       # Configuration de Spring Security
│   ├── CustomUserDetails.java    # Implémentation de UserDetails
│   └── CustomUserDetailsService.java # Chargement des utilisateurs
└── service/          # Services métier (logique applicative)
    ├── UserService.java        # Logique de gestion des utilisateurs
    ├── ResourceService.java    # Logique de gestion des ressources
    └── ReservationService.java # Logique de gestion des réservations
```

## Modèle de données

### Entités principales

#### User (Utilisateur)
- **Attributs** : id, nom, email, password, roles
- **Relations** : One-to-Many avec Reservation

#### Resource (Ressource)
- **Attributs** : id, nom, type
- **Relations** : One-to-Many avec Reservation

#### Reservation (Réservation)
- **Attributs** : id, dateDebut, dateFin, statut
- **Relations** : Many-to-One avec User et Resource
- **Statuts possibles** : ACTIVE, ANNULEE, EXPIREE

## API Endpoints détaillés

### Authentification et Utilisateurs

- `POST /api/users/register` - Inscription d'un nouvel utilisateur (public)
  - Corps de la requête : `{"nom": "...", "email": "...", "password": "..."}`
  - Réponse : 200 OK avec message de confirmation ou 400 Bad Request si l'email existe déjà

- `POST /api/users/login` - Connexion d'un utilisateur (public)
  - Corps de la requête : `{"email": "...", "password": "..."}`
  - Réponse : 200 OK avec message de confirmation ou 401 Unauthorized si identifiants incorrects

- `GET /api/users` - Liste des utilisateurs (ADMIN)
  - Réponse : 200 OK avec tableau d'objets utilisateur

- `GET /api/users/{id}` - Détails d'un utilisateur (ADMIN)
  - Réponse : 200 OK avec objet utilisateur ou 404 Not Found

- `POST /api/users` - Création d'un utilisateur (ADMIN)
  - Corps de la requête : `{"nom": "...", "email": "...", "password": "..."}`
  - Réponse : 200 OK avec utilisateur créé

- `DELETE /api/users/{id}` - Suppression d'un utilisateur (ADMIN)
  - Réponse : 204 No Content

### Ressources

- `GET /api/resources` - Liste des ressources (ADMIN, CLIENT)
  - Réponse : 200 OK avec tableau d'objets ressource

- `GET /api/resources/{id}` - Détails d'une ressource (ADMIN, CLIENT)
  - Réponse : 200 OK avec objet ressource ou 404 Not Found

- `POST /api/resources` - Création d'une ressource (ADMIN)
  - Corps de la requête : `{"nom": "...", "type": "..."}`
  - Réponse : 201 Created avec ressource créée

- `DELETE /api/resources/{id}` - Suppression d'une ressource (ADMIN)
  - Réponse : 204 No Content ou 409 Conflict si la ressource a des réservations

### Réservations

- `GET /api/reservations` - Liste des réservations (ADMIN)
  - Réponse : 200 OK avec tableau d'objets réservation

- `GET /api/reservations/{id}` - Détails d'une réservation (ADMIN, CLIENT propriétaire)
  - Réponse : 200 OK avec objet réservation ou 404 Not Found

- `GET /api/reservations/user/{userId}` - Réservations d'un utilisateur (ADMIN, CLIENT propriétaire)
  - Réponse : 200 OK avec tableau d'objets réservation

- `GET /api/reservations/resource/{resourceId}` - Réservations d'une ressource (ADMIN, CLIENT)
  - Réponse : 200 OK avec tableau d'objets réservation

- `POST /api/reservations` - Création d'une réservation (ADMIN, CLIENT)
  - Corps de la requête : `{"userId": 1, "resourceId": 1, "dateDebut": "2023-06-01T10:00:00", "dateFin": "2023-06-01T12:00:00"}`
  - Réponse : 201 Created avec réservation créée, 404 Not Found si utilisateur ou ressource non trouvé, ou 409 Conflict si conflit de réservation

- `DELETE /api/reservations/{id}` - Annulation d'une réservation (ADMIN, CLIENT propriétaire)
  - Réponse : 204 No Content

## Sécurité et bonnes pratiques

### Sécurité

- **Authentification** : Spring Security avec formulaire de connexion et authentification HTTP Basic
- **Autorisation** : Contrôle d'accès basé sur les rôles (ROLE_ADMIN, ROLE_CLIENT)
- **Protection des mots de passe** : Encodage avec BCrypt
- **Validation des entrées** : Validation des données avec Jakarta Bean Validation
- **Gestion des exceptions** : Traitement unifié des erreurs avec réponses HTTP appropriées

### Gestion de la concurrence

- **Synchronisation** : Utilisation de méthodes synchronized pour les opérations critiques
- **Sémaphores** : Limitation du nombre d'accès simultanés aux sections critiques
- **Transactions** : Utilisation de @Transactional pour garantir l'atomicité des opérations

### Bonnes pratiques implémentées

- **Architecture en couches** : Séparation claire des responsabilités
- **Pattern DTO** : Utilisation de DTOs pour découpler l'API des entités
- **Mappers** : Conversion explicite entre entités et DTOs
- **Documentation** : Documentation complète de l'API avec Swagger/OpenAPI
- **Logging** : Journalisation des opérations importantes
- **Nettoyage automatique** : Tâche planifiée pour maintenir la cohérence des données

## Installation et démarrage

### Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur
- MySQL 8.0 ou supérieur (ou MariaDB compatible)
- XAMPP (optionnel, pour une installation facile de MySQL)

### Préparation de la base de données

1. Créer une base de données MySQL nommée `ranblanc_db`
2. S'assurer que l'encodage est configuré en UTF-8 (utf8mb4)

### Configuration

Modifier le fichier `src/main/resources/application.properties` selon votre environnement :

```properties
# Configuration MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/ranblanc_db?characterEncoding=utf8&connectionCollation=utf8mb4_unicode_ci
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Encodage UTF-8
spring.datasource.hikari.connection-init-sql=SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci
```

### Compilation et exécution

```bash
# Cloner le dépôt (si ce n'est pas déjà fait)
git clone https://github.com/votre-username/blanc.git
cd blanc

# Compiler le projet
mvn clean package

# Exécuter l'application
java -jar target/blanc-0.0.1-SNAPSHOT.jar
```

L'application sera accessible à l'adresse : http://localhost:8080

La documentation Swagger sera disponible à : http://localhost:8080/swagger-ui.html

## Utilisation et tests

### Création d'un compte administrateur

Pour créer un compte administrateur initial, vous pouvez utiliser l'API d'inscription puis modifier le rôle en base de données, ou exécuter le script SQL suivant :

```sql
INSERT INTO users (nom, email, password, roles) 
VALUES ('Admin', 'admin@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN');
```

Ce script crée un utilisateur avec les identifiants :
- Email : admin@example.com
- Mot de passe : password

### Test des fonctionnalités

1. Accéder à l'interface Swagger : http://localhost:8080/swagger-ui.html
2. S'authentifier avec les identifiants administrateur
3. Explorer et tester les différents endpoints de l'API

## Évolutions futures

- Ajout d'une interface utilisateur frontend (Angular, React ou Vue.js)
- Implémentation de notifications par email pour les réservations
- Système de recherche avancée de ressources disponibles
- Rapports et statistiques d'utilisation des ressources
- Support pour l'authentification OAuth2 / OpenID Connect
- API mobile pour la gestion des réservations en déplacement

## Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. Forker le projet
2. Créer une branche pour votre fonctionnalité (`git checkout -b feature/ma-fonctionnalite`)
3. Committer vos changements (`git commit -m 'Ajout de ma fonctionnalité'`)
4. Pousser vers la branche (`git push origin feature/ma-fonctionnalite`)
5. Ouvrir une Pull Request

## Licence

Ce projet est sous licence [MIT](LICENSE).

## Contact

Pour toute question ou suggestion, veuillez contacter l'équipe de développement à l'adresse : contact@ranblanc.com
