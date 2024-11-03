package Interface;

import java.util.List;

import Products.Produit;
import database.ProduitDAO;
import javafx.application.Application;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestView extends Application {

	private Scene mainScene;
	
    @Override
    public void start(Stage primaryStage) {
    	// Barre supérieure avec nom du magasin

    	// Nom du magasin à gauche
    	Label Name = new Label("Shop");
    	Name.setStyle("-fx-text-fill: #333; -fx-font-size: 30px; -fx-font-weight: bold;");

    	// Organisation de la mise en page principale
        AnchorPane root = new AnchorPane();
        afficherInitialize(root);

        // Section principale
        VBox sectionProduits = new VBox();
        sectionProduits.getChildren().add(afficherProduits(primaryStage));

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
        root.getChildren().addAll(Name, sectionProduits, filtreBox, panierButton);
        
     // Créer le ScrollPane et y ajouter le GridPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true); // Le contenu s'adapte à la largeur de la fenêtre
        scrollPane.setPannable(true);   // Permet le défilement via la souris
        
        
        // Création de la scène et affichage
        mainScene = new Scene(root, 800, 600);
        
        //scene.getStylesheets().add("file:/Site/resources/style.css");
        String css = this.getClass().getResource("/style.css").toExternalForm();
        mainScene.getStylesheets().add(css);
        
        Image icon = new Image (getClass().getResource("/Image/logo.jpg").toExternalForm());
        
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Magasin de tennis");
        primaryStage.setScene(mainScene);
        primaryStage.setFullScreen(false); //A remettre vrai
        primaryStage.show();
    }
    
    public void afficherInitialize(AnchorPane root) {
    	// Icône du compte à droite (bouton avec icône)
    	ImageView accountIcon = new ImageView(new Image(getClass().getResource("/Image/accountIcon.png").toExternalForm()));
    	accountIcon.setFitHeight(30); // Ajuster la taille de l'image
    	accountIcon.setFitWidth(30); 
    	Button accountButton = new Button();
    	
    	//Placer l'icone du compte en haut à droite
        AnchorPane.setTopAnchor(accountButton, 0.0);
        AnchorPane.setRightAnchor(accountButton, 10.0);
        
    	accountButton.setGraphic(accountIcon); // Placer l'icône dans le bouton
    	accountButton.setStyle("-fx-background-color: transparent;"); // Enlever le fond pour le bouton
    	
    	// Changement du curseur lors du survol du produit
        accountButton.setOnMouseEntered(event -> accountButton.setCursor(javafx.scene.Cursor.HAND));
        accountButton.setOnMouseExited(event -> accountButton.setCursor(javafx.scene.Cursor.DEFAULT));
    	
    	
    	// Création de la barre de navigation
        MenuBar menuBar = new MenuBar();
        Menu menuVetements = new Menu("Vêtements");
        Menu menuSacs = new Menu("Sacs");
        Menu menuAccessoires = new Menu("Accessoires");
        menuBar.getMenus().addAll(menuVetements, menuSacs, menuAccessoires);
        
        // Placer la barre de menu en haut
        AnchorPane.setTopAnchor(menuBar, 50.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);
        
        root.getChildren().addAll(accountButton, menuBar);
    }
    
    public GridPane afficherProduits(Stage primaryStage) {
        GridPane produitsGrid = new GridPane();
        produitsGrid.setPadding(new Insets(10, 10, 10, 10));
        produitsGrid.setHgap(10);
        produitsGrid.setVgap(10);
        
        // Récupérer les produits depuis la base de données
        ProduitDAO produitDAO = new ProduitDAO();
        List<Produit> produits = produitDAO.getAllProduits();
        
        // Boucle pour ajouter chaque produit à la grille
        int colonne = 0;
        int ligne = 0;
        
        for (Produit produit : produits) {
            // Création des composants pour chaque produit
            ImageView imageView = new ImageView(new Image(getClass().getResource(produit.getImagePath()).toExternalForm()));
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);

            Label nomProduit = new Label(produit.getNom());
            nomProduit.getStyleClass().add("nom-produit"); // Appliquer la classe CSS
            Label prixProduit = new Label("Prix : " + produit.getPrice() + "€");
            prixProduit.getStyleClass().add("prix-produit"); // Appliquer la classe CSS

            VBox produitBox = new VBox();
            produitBox.getStyleClass().add("produit-box"); // Appliquer la classe CSS
            produitBox.getChildren().addAll(imageView, nomProduit, prixProduit);
            
            // Gestion du clic sur le produit pour afficher les détails
            produitBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    afficherDetailsProduit(primaryStage, produit);
                }
            });

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
    
 // Créer une scène de détails du produit
    public void afficherDetailsProduit(Stage primaryStage, Produit produit) {
        // Création d'un nouveau conteneur pour les détails du produit
        VBox detailsBox = new VBox();
        detailsBox.setPadding(new Insets(20));
        detailsBox.setSpacing(10);
        
        // Affichage des informations du produit
        Label nomLabel = new Label(produit.getNom());
        Label prixLabel = new Label("Prix : " + produit.getPrice() + "€");
        
        ImageView imageView = new ImageView(new Image(getClass().getResource(produit.getImagePath()).toExternalForm()));
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        
        // Ajouter les éléments à la boîte de détails
        detailsBox.getChildren().addAll(nomLabel, prixLabel, imageView);
        
        // Bouton pour revenir à la scène principale
        Button retourButton = new Button("Retour");
        retourButton.setOnAction(e -> primaryStage.setScene(mainScene));  // revenir à la scène principale
        detailsBox.getChildren().add(retourButton);
        
        // Nouvelle scène pour les détails
        Scene detailScene = new Scene(detailsBox, 800, 600);
        primaryStage.setScene(detailScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}