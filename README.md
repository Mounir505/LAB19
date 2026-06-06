Rapport de LAB 19 : Application de Gestion de Notes avec Room et MVVM
==========================================================================

**Auteur :** Étudiant en Développement Android

**Date :** Juin 2026

**Projet :** RoomMVVMDemo

* * *

1\. Introduction et Objectifs du Laboratoire
--------------------------------------------

Ce laboratoire avait pour but de concevoir une application Android d'enregistrement de notes en appliquant rigoureusement l'architecture recommandée par Google : **MVVM (Model-View-ViewModel)**. L'accent a été mis sur deux problématiques majeures du développement mobile :

*   La **persistance des données locale** à l'aide d'une base de données SQLite gérée par l'I.O.M. (Object-Relational Mapping) **Room**.
*   La **gestion du cycle de vie** de l'application, assurant que les données saisies ou affichées ne soient pas perdues lors des changements de configuration (comme la rotation de l'écran) grâce au composant **ViewModel** et au mécanisme d'observation **LiveData**.

* * *

2\. Architecture du Projet (MVVM)
---------------------------------

Le projet a été restructuré en paquets distincts afin de respecter la séparation des responsabilités. Voici le détail des composants implémentés :

### A. La Couche Modèle & Données (Package : `data`)

*   **L'Entité (Note.java) :** Représente la table de données SQLite. Chaque note possède un identifiant unique auto-généré (`@PrimaryKey`), un titre et une description.
*   **Le DAO (NoteDao.java) :** Interface définissant les requêtes SQL abstraites (`@Insert`, `@Delete`, et une requête personnalisée `@Query("DELETE FROM note_table")` pour la suppression globale). Les méthodes de lecture retournent un objet `LiveData<List<Note>>`, permettant une mise à jour réactive de l'interface.
*   **La Base de Données (NoteDatabase.java) :** Classe abstraite qui hérite de `RoomDatabase`. Elle implémente le pattern **Singleton** afin de garantir qu'une seule instance de la base de données ne soit ouverte simultanément, évitant ainsi les fuites de mémoire et les conflits d'accès.

### B. La Couche ViewModel (Package : `viewmodel`)

*   **NoteViewModel.java :** Ce composant sert de pont entre la base de données (modèle) et l'interface graphique (vue). Il hérite de `AndroidViewModel` pour disposer du contexte de l'application. Sa fonction critique est de survivre aux destructions/recréations de l'activité. C'est lui qui expose le flux `LiveData` des notes à la `MainActivity`.

### C. La Couche Vue & Interface (Package : `ui`)

*   **MainActivity.java :** Point d'entrée de l'application. Elle initialise les composants graphiques, instancie le `ViewModel`, et "observe" le `LiveData`. Dès que la base de données change, la liste se met à jour automatiquement sans rafraîchissement manuel.
*   **NoteAdapter.java :** Un adaptateur optimisé pour le `RecyclerView` utilisant `ListAdapter` et `DiffUtil`. Au lieu de recharger toute la liste à chaque modification (ce qui est lourd pour le processeur), il calcule la différence exacte entre l'ancienne et la nouvelle liste pour animer uniquement l'élément modifié ou supprimé.

* * *

3\. Journal de Résolution des Problèmes et Bugs (Dépannage)
-----------------------------------------------------------

Le développement de ce projet a fait face à plusieurs obstacles techniques critiques qui ont été résolus avec succès :

Symptôme du Bug

Cause Racine

Solution Appliquée

**Émulateur Pixel 5 bloqué :** Message indiquant qu'une instance s'exécute déjà (PID 32892).

Fermeture brutale d'Android Studio laissant un processus zombie (qemu) ou un fichier de verrouillage (.lock) actif sur l'ordinateur hôte.

Utilisation de la commande système `taskkill /F /PID 32892` (ou `kill -9`) pour forcer la libération du simulateur de l'appareil mobile.

**Échec Gradle :** Impossible de résoudre la dépendance `androidx.room:room-livedata:2.8.4`.

Erreur de documentation historique : cet artefact n'existe pas de manière autonome dans les dépôts de Google. Room intègre nativement le support LiveData dans son cœur.

Suppression de la ligne erronée dans le fichier `build.gradle.kts` et exécution d'un _Clean Project_ suivi d'un _Invalidate Caches & Restart_.

**Erreurs de compilation Java :** "Cannot resolve symbol" sur `fab`, `toolbar`, ou `nav_host_fragment`.

Conflit lors de la création du projet. Android Studio a injecté du code par défaut lié au modèle "Basic Views Activity", alors que notre fichier `activity_main.xml` était configuré sur mesure.

Nettoyage complet du fichier `MainActivity.java` pour ne lier que nos propres éléments graphiques (champs textes, boutons d'action et RecyclerView).

* * *

4\. Protocole de Validation (Scénario de la Vidéo Démo)
-------------------------------------------------------

La vidéo de démonstration fournie comme livrable suit précisément ce protocole pour prouver le fonctionnement de l'application au correcteur :

1.  **Test d'Insertion et Réactivité :** Saisie et ajout successif de trois notes distinctes. _Objectif :_ Montrer l'apparition immédiate des éléments dans le RecyclerView (Preuve du bon chaînage LiveData + Room).
2.  **Test de Cycle de Vie (Rotation) :** Saisie d'un texte temporaire dans le champ titre, puis rotation de l'émulateur en mode Paysage. _Objectif :_ Constater que la liste et le texte en cours de saisie restent intacts (Preuve de la persistance des données en mémoire vive par le ViewModel).
3.  **Test de Suppression Individuelle :** Clic long sur la deuxième note de la liste. _Objectif :_ Valider le fonctionnement du déclencheur d'événement personnalisé et l'animation de suppression du ListAdapter.
4.  **Test de Persistance Globale (Mort du processus) :** Fermeture complète et forcée de l'application via le gestionnaire des tâches Android, puis relance immédiate de l'application. _Objectif :_ Constater que les notes restantes sont toujours présentes à l'écran (Preuve absolue que Room a écrit les fichiers sur le disque dur SQLite).

> **Note technique importante pour le visionnage :** Les interactions tactiles ont été rendues visibles à l'écran en activant l'option de développement "Afficher les éléments choisis" (Show taps) sur l'appareil de test afin de certifier l'usage des clics longs.
> * * *

5\. Démonstration Vidéo
-----------------------

Une vidéo de démonstration a été réalisée afin de valider l'ensemble des fonctionnalités développées dans le cadre de ce laboratoire. Cette démonstration met en évidence le fonctionnement de l'architecture MVVM, l'intégration de Room, ainsi que le comportement réactif de l'interface utilisateur grâce à LiveData.

### Scénario de démonstration

#### 1. Ajout de nouvelles notes

- Saisie d'un titre et d'une description.
- Validation de l'enregistrement via le bouton d'ajout.
- Répétition de l'opération pour plusieurs notes.

**Résultat attendu :**

Chaque nouvelle note apparaît instantanément dans le RecyclerView sans actualisation manuelle de l'interface, démontrant le bon fonctionnement du mécanisme d'observation LiveData.

### 2. Mise à jour automatique de l'interface

- Observation du RecyclerView après chaque insertion.
- Vérification de l'ajout dynamique des éléments.

**Résultat attendu :**

Les modifications de la base Room sont immédiatement propagées à l'interface utilisateur via le ViewModel.

### 3. Test du cycle de vie de l'activité

- Saisie de données dans les champs de formulaire.
- Rotation de l'émulateur ou du périphérique (Portrait ↔ Paysage).

**Résultat attendu :**

Les données affichées restent disponibles après la recréation de l'activité, démontrant la persistance des informations gérées par le ViewModel.

### 4. Suppression individuelle d'une note

- Sélection d'une note existante.
- Déclenchement de l'action de suppression.

**Résultat attendu :**

La note disparaît immédiatement de la liste et le RecyclerView applique automatiquement les animations prévues par ListAdapter et DiffUtil.

### 5. Suppression complète des données

- Utilisation de l'option permettant de supprimer toutes les notes.
- Vérification de l'état de la liste après l'opération.

**Résultat attendu :**

L'ensemble des enregistrements est supprimé de la base de données et l'interface se met à jour automatiquement.

### 6. Validation de la persistance Room

- Fermeture complète de l'application.
- Relance de l'application après arrêt du processus.

**Résultat attendu :**

Les notes précédemment enregistrées sont restaurées depuis la base SQLite gérée par Room, confirmant la persistance locale des données.

### Éléments techniques observés durant la démonstration

- Communication entre l'interface utilisateur et le ViewModel.
- Observation réactive des données via LiveData.
- Persistance des données à l'aide de Room.
- Optimisation de l'affichage avec RecyclerView, ListAdapter et DiffUtil.
- Conservation des données lors des changements de configuration.

### Lien de la vidéo



https://github.com/user-attachments/assets/8e4f2d71-cf13-401f-b23b-50185258d64c


* * *
