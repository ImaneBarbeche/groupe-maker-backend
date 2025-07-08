# 🎲 Group Maker - Application de Création de Groupes

## � Vue d'ensemble

Group Maker est une application Spring Boot conçue pour créer automatiquement des groupes d'apprenants en prenant en compte différents critères de mixité et l'historique des formations précédentes.

## ✨ Fonctionnalités Implémentées

### 🔐 Authentification & Autorisation
- ✅ Inscription avec validation email (simulation)
- ✅ Connexion/déconnexion avec JWT
- ✅ Système de rôles (USER/ADMIN)
- ✅ Compte désactivé par défaut selon cahier des charges

### 👥 Gestion des Utilisateurs
- ✅ Profil utilisateur complet
- ✅ Modification des informations
- ✅ Suppression de compte
- ✅ Acceptation des CGU avec date

### � Gestion des Listes
- ✅ Création/modification/suppression de listes
- ✅ Listes personnelles par utilisateur
- ✅ Nom unique par utilisateur

### 👤 Gestion des Personnes
- ✅ Champs conformes au cahier des charges :
  - Nom (3-50 caractères)
  - Genre (énumération : Masculin, Féminin, Ne se prononce pas)
  - Aisance en Français (1-4)
  - Ancien DWWM (Boolean)
  - Niveau technique (1-4)
  - Profil (énumération : Timide, Réservé, À l'aise)
  - Âge (1-99 ans selon spécifications)

### 🎲 Génération Aléatoire de Groupes
- ✅ Algorithme intelligent de création de groupes
- ✅ Critères de mixité configurables :
  - Mixer les genres
  - Mixer les âges
  - Mixer les niveaux techniques
  - Mixer l'aisance en français
  - Mixer anciens/nouveaux DWWM
  - Mixer les profils
- ✅ Prise en compte de l'historique pour éviter les doublons
- ✅ Nommage personnalisé des groupes
- ✅ Score de qualité des groupes générés

### � Administration
- ✅ Panel d'administration (ROLE_ADMIN requis)
- ✅ Gestion des utilisateurs
- ✅ Statistiques détaillées :
  - Nombre total d'utilisateurs, listes, tirages
  - Moyennes par utilisateur/liste
  - Statistiques par utilisateur

### �️ Historique
- ✅ Sauvegarde automatique des tirages
- ✅ Consultation de l'historique
- ✅ Utilisation pour éviter les répétitions

## 🏗️ Architecture Technique

### Backend
- **Framework** : Spring Boot 3.5.0
- **Base de données** : PostgreSQL
- **Sécurité** : Spring Security + JWT
- **ORM** : JPA/Hibernate
- **Documentation** : OpenAPI/Swagger

### Modèles de Données
```
Utilisateur (nom, prénom, email, mot de passe, rôle, actif, dates)
├── Liste (nom, slug, description)
│   ├── Personne (nom, genre, aisance, ancien, niveau, profil, âge)
│   └── Groupe (nom, critères, date, personnes[])
├── Historique (date, nom liste, groupes[])
└── PartagesList (propriétaire, partagé avec, droits)
```

## � API Endpoints

### Authentification
```
POST /utilisateurs/register - Inscription
GET  /utilisateurs/activate/{email} - Activation compte
POST /utilisateurs/login - Connexion
POST /utilisateurs/logout - Déconnexion
```

### Listes & Personnes
```
GET    /listes/mine - Mes listes
POST   /listes - Créer une liste
PUT    /listes/{id} - Modifier une liste
DELETE /listes/{id} - Supprimer une liste
POST   /listes/{id}/personnes - Ajouter des personnes
```

### Groupes
```
GET  /groupes/liste/{listeId} - Groupes d'une liste
POST /groupes/liste/{listeId} - Créer un groupe
POST /groupes/liste/{listeId}/aleatoire - Génération aléatoire
DELETE /groupes/{id} - Supprimer un groupe
```

### Administration (ADMIN seulement)
```
GET    /admin/utilisateurs - Liste des utilisateurs
DELETE /admin/utilisateurs/{id} - Supprimer utilisateur
PUT    /admin/utilisateurs/{id}/role - Changer rôle
GET    /admin/statistiques - Statistiques globales
GET    /admin/utilisateurs/statistiques - Stats par utilisateur
```

## 🚀 Installation & Démarrage

### Prérequis
- Java 17+
- PostgreSQL
- Maven

### Configuration
1. Créer une base de données PostgreSQL `groupe_maker`
2. Configurer `application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/groupe_maker
spring.datasource.username=votre_username
spring.datasource.password=votre_password
jwt.secret=votre_secret_jwt_base64
```

### Démarrage
```bash
mvn clean install
mvn spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## 📝 Fonctionnalités Manquantes (À Implémenter)

### ❌ En cours de développement
- [ ] Système d'envoi d'emails réels
- [ ] Partage de listes entre utilisateurs
- [ ] Interface web (SPA)
- [ ] Pages légales
- [ ] Système de glisser-déposer pour réorganiser
- [ ] Renouvellement automatique des CGU (13 mois)
- [ ] Séparation en deux bases de données
- [ ] CI/CD et environnement de test
- [ ] Documentation Wiki

## 🧪 Tests

Pour lancer les tests :
```bash
mvn test
```

## 📊 Score de Conformité au Cahier des Charges

**Progression actuelle : ~65%**

✅ **Complété** :
- Modèles de données conformes
- Authentification/autorisation robuste
- Algorithme de génération intelligente
- Administration fonctionnelle
- API REST complète

🔄 **En développement** :
- Interface utilisateur
- Système de partage
- Validation email réelle
- Architecture distribuée

## 🤝 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/nom-feature`)
3. Commit (`git commit -m 'Ajout nouvelle fonctionnalité'`)
4. Push (`git push origin feature/nom-feature`)
5. Ouvrir une Pull Request
