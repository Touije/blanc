# API Endpoints pour RanBlanc

Ce document liste tous les endpoints API disponibles pour l'intégration frontend.

Tous les endpoints commencent par la base URL : `http://localhost:8080`

## Authentification

### Inscription

```
POST http://localhost:8080/api/users/register
```

**Corps de la requête:**
```json
{
  "nom": "Nom de l'utilisateur",
  "prenom": "Prénom de l'utilisateur", // Optionnel
  "telephone": "Numéro de téléphone", // Optionnel
  "email": "utilisateur@example.com",
  "password": "motdepasse"
}
```

**Réponse:**
```json
{
  "message": "Inscription réussie",
  "user": {
    "id": 1,
    "nom": "Nom de l'utilisateur",
    "prenom": "Prénom de l'utilisateur",
    "telephone": "Numéro de téléphone",
    "email": "utilisateur@example.com"
  }
}
```

### Connexion

```
POST http://localhost:8080/api/users/login
```

**Corps de la requête:**
```json
{
  "email": "utilisateur@example.com",
  "password": "motdepasse"
}
```

**Réponse (utilisateur existant):**
```json
{
  "message": "Connexion réussie",
  "user": {
    "id": 1,
    "nom": "Nom de l'utilisateur",
    "prenom": "Prénom de l'utilisateur",
    "telephone": "Numéro de téléphone",
    "email": "utilisateur@example.com"
  },
  "isNewUser": false
}
```

**Réponse (nouvel utilisateur):**
```json
{
  "message": "Inscription et connexion réussies",
  "user": {
    "id": 1,
    "nom": "Nom de l'utilisateur",
    "prenom": "Prénom de l'utilisateur",
    "telephone": "Numéro de téléphone",
    "email": "utilisateur@example.com"
  },
  "isNewUser": true
}
```

## Gestion des Utilisateurs (Admin uniquement)

### Récupérer tous les utilisateurs

```
GET http://localhost:8080/api/users
```

### Récupérer un utilisateur par ID

```
GET http://localhost:8080/api/users/{id}
```

### Créer un utilisateur

```
POST http://localhost:8080/api/users
```

**Corps de la requête:**
```json
{
  "nom": "Nom de l'utilisateur",
  "prenom": "Prénom de l'utilisateur",
  "telephone": "Numéro de téléphone",
  "email": "utilisateur@example.com",
  "password": "motdepasse"
}
```

### Supprimer un utilisateur

```
DELETE http://localhost:8080/api/users/{id}
```

### Mettre à jour le rôle d'un utilisateur

```
PUT http://localhost:8080/api/admin/users/{id}/role
```

**Corps de la requête:**
```json
{
  "role": "ADMIN" // ou "CLIENT"
}
```

## Gestion des Ressources (Admin uniquement)

### Récupérer toutes les ressources

```
GET http://localhost:8080/api/resources
```

### Récupérer une ressource par ID

```
GET http://localhost:8080/api/resources/{id}
```

### Créer une ressource

```
POST http://localhost:8080/api/resources
```

**Corps de la requête:**
```json
{
  "nom": "Nom de la ressource",
  "type": "Type de la ressource",
  "description": "Description détaillée",
  "disponible": true,
  "localisation": "Emplacement de la ressource"
}
```

### Mettre à jour une ressource

```
PUT http://localhost:8080/api/resources/{id}
```

**Corps de la requête:**
```json
{
  "nom": "Nouveau nom",
  "type": "Nouveau type",
  "description": "Nouvelle description",
  "disponible": true,
  "localisation": "Nouvel emplacement"
}
```

### Supprimer une ressource

```
DELETE http://localhost:8080/api/resources/{id}
```

## Gestion des Réservations (Admin et Client)

### Récupérer toutes les réservations

```
GET http://localhost:8080/api/reservations
```

### Récupérer une réservation par ID

```
GET http://localhost:8080/api/reservations/{id}
```

### Créer une réservation

```
POST http://localhost:8080/api/reservations
```

**Corps de la requête:**
```json
{
  "userId": 1,
  "resourceId": 1,
  "dateDebut": "2023-06-01T10:00:00",
  "dateFin": "2023-06-01T12:00:00",
  "statut": "ACTIVE"
}
```

### Mettre à jour une réservation

```
PUT http://localhost:8080/api/reservations/{id}
```

**Corps de la requête:**
```json
{
  "dateDebut": "2023-06-01T11:00:00",
  "dateFin": "2023-06-01T13:00:00",
  "statut": "ACTIVE"
}
```

### Annuler une réservation

```
PUT http://localhost:8080/api/reservations/{id}/cancel
```

### Supprimer une réservation

```
DELETE http://localhost:8080/api/reservations/{id}
```

## Tableau de bord Admin

### Récupérer les statistiques du tableau de bord

```
GET http://localhost:8080/api/admin/dashboard
```

**Réponse:**
```json
{
  "userCount": 10,
  "resourceCount": 5,
  "activeReservationCount": 3
}
```

## Notes d'utilisation

1. Tous les endpoints nécessitent une authentification, sauf `http://localhost:8080/api/users/register` et `http://localhost:8080/api/users/login`.
2. Les endpoints `http://localhost:8080/api/users/**` et `http://localhost:8080/api/resources/**` sont accessibles uniquement aux administrateurs.
3. Les endpoints `http://localhost:8080/api/reservations/**` sont accessibles aux administrateurs et aux clients.
4. Les endpoints `http://localhost:8080/api/admin/**` sont accessibles uniquement aux administrateurs.
5. L'authentification se fait via Basic Auth ou via le formulaire de connexion.