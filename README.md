# SuperNotes

| __GitHub Actions__ | [![Build](https://github.com/dounyaa/SuperNotes/actions/workflows/gradle.yml/badge.svg)](https://github.com/dounyaa/SuperNotes/actions/workflows/gradle.yml) |
| :--- | :--- |
| __Version__ | [![Tag](https://img.shields.io/badge/Tag-v0.2-blue)](https://github.com/dounyaa/SuperNotes/releases/tag/v0.2) |
| __SonarCloud__ | [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=dounyaa_SuperNotes&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=dounyaa_SuperNotes) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=dounyaa_SuperNotes&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=dounyaa_SuperNotes) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=dounyaa_SuperNotes&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=dounyaa_SuperNotes) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=dounyaa_SuperNotes&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=dounyaa_SuperNotes) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=dounyaa_SuperNotes&metric=bugs)](https://sonarcloud.io/summary/new_code?id=dounyaa_SuperNotes) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=dounyaa_SuperNotes&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=dounyaa_SuperNotes) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=dounyaa_SuperNotes&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=dounyaa_SuperNotes) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=dounyaa_SuperNotes&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=dounyaa_SuperNotes) |
| __Licence__ | [![license: Apache 2.0](https://img.shields.io/badge/license-Apache_2.0-green)](LICENSE) |


## Description

SuperNote est un plugin polyvalent de gestion de notes conçu pour offrir une expérience de prise de notes rapide et efficace. Il peut être utilisé en ligne de commande, et il est destiné à fonctionner avec diverses plateformes de prise de notes et environnements de développement intégrés (IDE).

---

## Exigences

- Java (version 21)
- Gradle (version 8.4)

---

## Installation

1. **Clonage du Projet :**

   ```bash
   git clone https://github.com/dounyaa/SuperNotes.git
   ```

2. **Compilation :**
- Sous (macOS/Linux)
   ```bash
   cd SuperNotes
   ./gradlew build
   ```
- Sous Windows
   ```bash
   cd SuperNotes
   gradlew.bat build
   ```

3. **Exécution :**
- Sous (macOS/Linux)
   ```bash
   ./gradlew run
   ```
- Sous Windows
   ```bash
   gradlew.bat run
   ```

---

## Utilisation

Une fois l'application lancée, voici quelques exemples de commandes pour interagir avec SuperNote :

- **Ajouter une Note :**
    ```bash
    sn add "Contenu de la note" --tag "tag"
    ```

- **Exporter les Notes dans un fichier pdf:**
    ```bash
    sn export --all "chemin/vers/le/fichier.pdf"
    ```

---
## Version v0.2

La version v0.2 de SuperNote propose les fonctionnalités suivantes :

### Création et Géstion des Notes
- **Créer une Note de type texte:** Permet de créer une nouvelle note avec un contenu spécifié et l'associe à un tag.
  ```bash
  sn add "Contenu de la note"

- **Ajouter une Note de type texte avec un Tag :** Permet de créer une nouvelle note avec un contenu spécifié et l'associe à un tag.
  ```bash
  sn add "Contenu de la note" --tag mon_tag

- **Créer une Note de type Image:** Permet de créer une nouvelle note avec un contenu spécifié et l'associe à un tag.
  ```bash
  sn add "chemin/vers/image.png"

- **Ajouter une Note de type image avec un Tag :** Permet de créer une nouvelle note avec un contenu spécifié et l'associe à un tag.
  ```bash
  sn add "chemin/vers/image.png" --tag "mon_tag"

- **AEnregistrer les Notes dans une Base de Données SQLite :**  Stocke toutes les notes et leurs tags associés dans une base de données SQLite.

- **Supprimer les notes par Tag :** Permet de supprimer toutes les notes associées à un tag spécifique.
spécifié et l'associe à un tag.
  ```bash
  sn delete --tag "mon_tag_a_supprimer"

### Générer un Rapport de Notes
- **Générer un Rapport de Toutes les Notes :** Crée un rapport sous format PDF contenant toutes les notes existantes.
spécifié et l'associe à un tag.
  ```bash
  sn export --all "chemin/vers/fichier.pdf"

- **Filtrer par Tag pour Générer le Rapport :** Génère un rapport PDF basé sur les notes associées à un tag spécifique.
  ```bash
  sn export --tag "mon_tag" "chemin/vers/fichier.pdf"

- **Filtrer par Mot dans le Contenu pour Générer le Rapport :** Crée un rapport PDF basé sur les notes contenant un mot spécifique.
  ```bash
  sn export --word "mot_a_chercher" "chemin/vers/fichier.pdf"

**Fermer l'Application :**
    ```bash
    exit
    ```

Cette version offre la possibilité de créer, gérer, filtrer et générer des rapports pour vos notes, en utilisant des tags et des mots-clés pour une organisation avancée et une gestion efficace.
---

## Version v0.3

La version v0.3 de SuperNote propose les fonctionnalités suivantes :

###Prérequis
- **Clé API Notion :** Pour bénéficier des fonctionnalités intégrées de Notion, une clé API Notion est nécessaire. Vous pouvez obtenir votre clé API depuis votre compte Notion afin d'accéder à ces fonctionnalités. Pour créer votre clé API :

- Accédez à cette page : https://www.notion.so/my-integrations.
- Cliquez sur "New Integration".
- Nommez votre intégration "SuperNotes" et appuyez sur "Submit".
- Félicitations ! Vous avez désormais votre clé API. Conservez-la en lieu sûr, vous en aurez besoin pour configurer SuperNotes avec Notion.

De plus, pour configurer correctement votre page Notion avec SuperNotes :

- Connectez-vous à Notion via ce lien : https://www.notion.so.
- Créez une nouvelle page en cliquant sur "Add New Page" dans le menu de gauche.
- Sur cette nouvelle page, cliquez sur les trois points en haut à droite.
- Descendez jusqu'à trouver "Add connections", cliquez dessus.
- Recherchez "SuperNotes" (il doit avoir le même nom que celui utilisé pour la clé API).

Dans l'URL de la page Notion que vous venez de créer, copiez les 32 derniers caractères après le dernier "/" : par exemple, si l'URL est https://www.notion.so/ma-page-**8210366e115d4a8d8f20d151c7e95308**, l'ID de la page est 8210366e115d4a8d8f20d151c7e95308. Conservez cet ID avec votre clé API, vous en aurez besoin pour l'intégration.

Ces étapes garantissent la liaison efficace entre SuperNotes et votre espace Notion, offrant ainsi une utilisation optimale des fonctionnalités.

### Intégration de l'API Notion

- **Créer une Note sur Notion:** Ajoute la fonctionnalité de création de notes directement sur Notion en utilisant la commande suivante.
  ```bash
  sn notion create "Contenu de la note"

- **Mettre à jour le Contenu d'une Note sur Notion :** Permet de mettre à jour le contenu d'une note existante sur Notion en utilisant le contenu actuel de la note et le nouveau contenu spécifié :
  ```bash
  sn notion update "Ancien contenu de la note" --note "Nouveau contenu de la note"

- **Enregister le Contenu d'une Page Notion dans Supernotes :** Permet de récupérer le contenu d'une page Notion spécifique :
  ```bash
  sn notion get --page "ID_de_la_page_Notion"

- **Exporter les notes créer sur notion :** Permet de créer un fichier avec toutes les notes créer sur notion avec supernotes
  ```bash
  sn export --tag "notion" "chemin/vers/fichier.pdf"

- **AEnregistrer les Notes dans une Base de Données SQLite :**  Stocke toutes les notes créer sur notion avec un id_page et parent_id_page.


### Commande Help

- **Afficher l'Aide :**  La commande help peut être utilisée pour afficher les commandes disponibles et leurs descriptions.
  ```bash
  sn --help


## Groupe

- Dounya Alaoui
- Aya LAKEHAL
