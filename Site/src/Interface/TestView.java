package Interface;

import java.util.List;

import Products.Produit;
import database.ProduitDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestView extends Application {

    @Override
    public void start(Stage primaryStage) {
    	// Barre supérieure avec nom du magasin et icône de compte

    	// Nom du magasin à gauche
    	Label Name = new Label("Shop");
    	Name.setStyle("-fx-text-fill: #333; -fx-font-size: 30px; -fx-font-weight: bold;");

    	// Icône du compte à droite (soit une image, soit un bouton avec icône)
    	ImageView accountIcon = new ImageView(new Image(getClass().getResource("/Image/Image1.png").toExternalForm())); // Remplace avec ton image
    	accountIcon.setFitHeight(30); // Ajuster la taille de l'image
    	accountIcon.setFitWidth(30); 

    	// Tu peux aussi utiliser un Button pour l'icône de compte avec une action :
    	Button accountButton = new Button();
    	accountButton.setGraphic(accountIcon); // Placer l'icône dans le bouton
    	accountButton.setStyle("-fx-background-color: transparent;"); // Enlever le fond pour le bouton

    	// Organisation de la mise en page principale
        AnchorPane root = new AnchorPane();
    	
        // Création de la barre de navigation
        MenuBar menuBar = new MenuBar();
        Menu menuVetements = new Menu("Vêtements");
        Menu menuSacs = new Menu("Sacs");
        Menu menuAccessoires = new Menu("Accessoires");
        menuBar.getMenus().addAll(menuVetements, menuSacs, menuAccessoires);

        // Section principale
        VBox sectionProduits = new VBox();
        sectionProduits.getChildren().add(afficherProduits());

        // Création d'un panneau latéral pour les filtres
        VBox filtreBox = new VBox();
        Label filtreTaille = new Label("Tailles");
        CheckBox tailleS = new CheckBox("Taille S");
        CheckBox tailleM = new CheckBox("Taille M");
        CheckBox tailleL = new CheckBox("Taille L");
        filtreBox.getChildren().addAll(filtreTaille, tailleS, tailleM, tailleL);
        
        Label filtreSexe = new Label("Sexe");
        CheckBox SexeHomme = new CheckBox("Hommes");
        CheckBox SexeFemme = new CheckBox("Femmes");
        CheckBox Enfant = new CheckBox("Enfants");
        filtreBox.getChildren().addAll(filtreSexe, SexeHomme, SexeFemme, Enfant);

        // Section du panier
        Button panierButton = new Button("Panier");

        //Placer le nom en haut à gauche
        AnchorPane.setTopAnchor(Name, 0.0);
        AnchorPane.setLeftAnchor(Name, 10.0);
        
        //Placer l'icone du compte en haut à droite
        AnchorPane.setTopAnchor(accountButton, 0.0);
        AnchorPane.setRightAnchor(accountButton, 10.0);
        
        // Placer la barre de menu en haut
        AnchorPane.setTopAnchor(menuBar, 50.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);

        // Placer la section des produits au centre
        AnchorPane.setTopAnchor(sectionProduits, 120.0);
        AnchorPane.setLeftAnchor(sectionProduits, 10.0);
        AnchorPane.setRightAnchor(sectionProduits, 200.0); // Ajuster la largeur du côté droit pour les filtres

        // Placer la boîte de filtres à droite
        AnchorPane.setTopAnchor(filtreBox, 120.0);
        AnchorPane.setRightAnchor(filtreBox, 10.0);

        // Placer le bouton panier en bas
        AnchorPane.setBottomAnchor(panierButton, 10.0);
        AnchorPane.setLeftAnchor(panierButton, 100.0);

        // Ajouter les éléments à l'AnchorPane
        root.getChildren().addAll(Name, accountButton, menuBar, sectionProduits, filtreBox, panierButton);
        
     // Créer le ScrollPane et y ajouter le GridPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true); // Le contenu s'adapte à la largeur de la fenêtre
        scrollPane.setPannable(true);   // Permet le défilement via la souris
        
        // Création de la scène et affichage
        Scene scene = new Scene(root, 800, 600);
        //scene.getStylesheets().add("file:/Site/resources/style.css");
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        
        primaryStage.setTitle("Boutique de vêtements");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false); //A remettre vrai
        primaryStage.show();
    }
    
    public GridPane afficherProduits() {
        GridPane produitsGrid = new GridPane();
        produitsGrid.setPadding(new Insets(10, 10, 10, 10));
        produitsGrid.setHgap(10);
        produitsGrid.setVgap(10);

     // Boucle pour ajouter chaque produit à la grille
        int colonne = 0;
        int ligne = 0;
     // Récupérer les produits depuis la base de données
        ProduitDAO produitDAO = new ProduitDAO();
        List<Produit> produits = produitDAO.getAllProduits();
        
        for (Produit produit : produits) {
            // Création des composants pour chaque produit
            Image image = new Image(getClass().getResource(produit.getImagePath()).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);

            Label nomProduit = new Label(produit.getNom());
            Label prixProduit = new Label("Prix : " + produit.getPrice() + "€");

            VBox produitBox = new VBox();
            produitBox.setAlignment(Pos.CENTER);
            produitBox.getChildren().addAll(imageView, nomProduit, prixProduit);

            // Ajouter le produit à la grille
            produitsGrid.add(produitBox, colonne, ligne);

            // Gestion des positions (on remplit d'abord les colonnes, puis les lignes)
            colonne++;
            if (colonne > 2) { // Par exemple, 3 produits par ligne
                colonne = 0;
                ligne++;
            }
        }

        return produitsGrid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}