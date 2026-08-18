# MedicareHubWeb
1. Pré-diagnostic médical

Responsable : Ouelbani Emna
Fonctionnalités :
Correspondance avec la base de données
Génération de questions dynamiques
Questions de clarification générées selon la logique des symptômes
Calcul d’un score de similarité
Affichage des pourcentages d’atteinte des maladies

2. Évaluation du risque de développer certaines maladies (profil patient)

Responsable : Krayni Afrah
Fonctionnalités :
Formulaire d’informations personnelles et antécédents médicaux
Calcul d’un score de risque personnalisé
Prédiction de maladie avec affichage d’un pourcentage de probabilité

3. Analyse des risques liés aux facteurs environnementaux
Responsable : Mansouri Omar
Fonctionnalités :

Carte interactive (Leaflet) centrée sur la position sélectionnée
Affichage de la qualité de l'air (indice AQI via WAQI)
Données météo en temps réel (température, vent, conditions via Open-Meteo)
Affichage des centrales industrielles à proximité (rayon 5 km) avec estimation des émissions CO₂
Calcul d'un score de risque environnemental agrégé (pollution + météo + industrie) sur 9 points
Sauvegarde des points cliqués en base de données avec leur score de risque
Affichage du score moyen historique pour un point géographique donné


Lancer le projet en local
Backend
cd Medicare-back
./mvnw spring-boot:run
Frontend
cd Medicare-front
npm install
npm start

Changelog
R3 — Mise en production
Implémentation de la fonctionnalité de sauvegarde et d'historique des points cliqués sur la carte, avec calcul et affichage du score de risque environnemental par point géographique.
R2
Développement du moteur de calcul de risque environnemental (pollution, météo, industrie) côté backend et intégration de la carte interactive côté frontend.
R1
Initialisation du projet, mise en place de l'architecture frontend React et backend Spring Boot, connexion à la base de données PostgreSQL.