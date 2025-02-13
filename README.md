# Site de vente en ligne
## Auteur
Pauline Fourel

## Installation
Assurez-vous d'avoir installé :
- Java version 17
- Javafx
- MySQL
Télécharger le dossier Fourel.zip en local.

Executer les scripts create_database.sql et insert_data (que vous trouverez dans le dossier doc) pour créer et insérer les données dans la base de données.

## Execution
- Ouvrir le terminal
- Placer vous dans le dossier où vous avez téléchargé le fichier avec la commande cd
- Executer le jar avec la commande : java --module-path "C:\Program Files\javafx-sdk-17.0.13\lib" --add-modules javafx.controls,javafx.fxml -jar target/Site-0.0.1-SNAPSHOT.jar
    * Si besoin, modifier le chemin C:\Program Files\javafx-sdk-17.0.13\lib
- Dans "Site\src\database\DatabaseConnection.java", modifier le nom d'utilisateur et le mode de passe avec ceux de votre base de données.