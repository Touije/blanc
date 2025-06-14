# RanBlanc – Système de Réservation de Ressources

Application de gestion de réservation de ressources développée avec Spring Boot.

## Fonctionnalités

- Authentification et autorisation avec Spring Security
- Gestion des utilisateurs (ADMIN, CLIENT)
- Gestion des ressources
- Réservation de ressources avec gestion des conflits
- Nettoyage automatique des réservations expirées
- Documentation API avec Swagger/OpenAPI

## Technologies utilisées

- Java 17
- Spring Boot 3.5.0
- Spring Security
- Spring Data JPA
- MySQL
- Lombok
- Swagger/OpenAPI

## Structure du projet

```
src/main/java/com/ranblanc/blanc/
├── config/           # Configuration (Swagger, etc.)
├── controller/       # Contrôleurs REST
├── dto/              # Objets de transfert de données
├── entity/           # Entités JPA
├── mapper/           # Convertisseurs entité <-> DTO
├── repository/       # Repositories JPA
├── scheduler/        # Tâches planifiées
├── security/         # Configuration de sécurité et JWT
└── service/          # Services métier
```

## API Endpoints

### Utilisateurs

- `GET /api/users` - Liste des utilisateurs (ADMIN)
- `GET /api/users/{id}` - Détails d'un utilisateur (ADMIN)
- `POST /api/users` - Création d'un utilisateur (ADMIN)
- `DELETE /api/users/{id}` - Suppression d'un utilisateur (ADMIN)

### Ressources

- `GET /api/resources` - Liste des ressources (ADMIN, CLIENT)
- `GET /api/resources/{id}` - Détails d'une ressource (ADMIN, CLIENT)
- `POST /api/resources` - Création d'une ressource (ADMIN)
- `DELETE /api/resources/{id}` - Suppression d'une ressource (ADMIN)

### Réservations

- `GET /api/reservations` - Liste des réservations (ADMIN)
- `GET /api/reservations/{id}` - Détails d'une réservation (ADMIN, CLIENT propriétaire)
- `GET /api/reservations/user/{userId}` - Réservations d'un utilisateur (ADMIN, CLIENT propriétaire)
- `GET /api/reservations/resource/{resourceId}` - Réservations d'une ressource (ADMIN, CLIENT)
- `POST /api/reservations` - Création d'une réservation (ADMIN, CLIENT)
- `PUT /api/reservations/{id}/cancel` - Annulation d'une réservation (ADMIN, CLIENT propriétaire)

## Sécurité

- Authentification basée sur Spring Security (Basic Auth et Form Login)
- Autorisations basées sur les rôles (ADMIN, CLIENT)
- Protection contre les conflits de réservation avec gestion de concurrence

## Installation et démarrage

### Prérequis

- Java 17 ou supérieur
- Maven
- MySQL

### Configuration

Modifier le fichier `application.properties` pour configurer la base de données et les paramètres JWT :

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ranblanc
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

### Compilation et exécution

```bash
# Compiler le projet
mvn clean package

# Exécuter l'application
java -jar target/blanc-0.0.1-SNAPSHOT.jar
```

L'application sera accessible à l'adresse : http://localhost:8080

La documentation Swagger sera disponible à : http://localhost:8080/swagger-ui.html

## Licence

Ce projet est sous licence [MIT](LICENSE).
