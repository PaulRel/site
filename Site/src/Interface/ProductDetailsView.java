package Interface;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import products.Chaussures;
import products.Produit;

public class ProductDetailsView {
	public ProductDetailsView(MainView mainView, Stage primaryStage, Produit produit) {
		showProductDetails(mainView, primaryStage, produit);
	}
	/**
     * Affiche les détails d'un produit spécifique dans une nouvelle scène.
     * 
     * @param primaryStage La scène principale de l'application.
     * @param produit      Le produit dont les détails sont à afficher.
     */
    public void showProductDetails(MainView mainView, Stage primaryStage, Produit produit) {
        // Création d'un nouveau conteneur pour les détails du produit
        HBox detailsBox = new HBox();
        detailsBox.setPadding(new Insets(20));
        detailsBox.setSpacing(10);
        detailsBox.setMaxSize(1200, 500);
        AnchorPane.setTopAnchor(detailsBox, 116.0);
        
        // Affichage des informations du produit
        Label nomLabel = new Label(produit.getNom());
        Label prixLabel = new Label("Prix : " + produit.getPrice() + "€");
        Label descriptionLabel = new Label(produit.getDescription() + "\n Choisir la taille :");
        descriptionLabel.setStyle("-fx-font-weight: normal");
        descriptionLabel.setWrapText(true);
        
        ComboBox<String> taillesComboBox = new ComboBox<>();
        
        // ComboBox pour afficher les tailles et quantités disponibles
        if (produit instanceof Chaussures) {
        	Chaussures chaussure = (Chaussures) produit;
        	HashMap<String, Integer> taillesStock = chaussure.getTailleStock();
        	System.out.println("Map " + taillesStock);
        	
        	 // Ajouter les éléments formatés dans la ComboBox
            for (Map.Entry<String, Integer> entry : taillesStock.entrySet()) {
                String taille = entry.getKey();
                int qtDispo = entry.getValue();
                String texte;
                if (qtDispo <= 5) {
                    texte = "Plus que " + qtDispo + " en stock";
                } else {
                    texte = "Stock 5+";
                }
                taillesComboBox.getItems().add(taille + " : " + texte);
            }

            // Écouter les changements de sélection dans la ComboBox
            taillesComboBox.setOnAction(event -> {
                
            });
        }
        	
        
        ImageView imageView = new ImageView(new Image(getClass().getResource(produit.getImagePath()).toExternalForm()));
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        
        Button addToCartButton = new Button("Ajouter au panier");
        addToCartButton.setOnAction(e -> new CartController(mainView, primaryStage));
        
        VBox descriptionBox = new VBox (nomLabel, prixLabel, descriptionLabel, taillesComboBox, addToCartButton);
        
        // Ajouter les éléments à la boîte de détails
        detailsBox.getChildren().addAll(imageView, descriptionBox);
        
        HeaderView v=new HeaderView(mainView, primaryStage);
        AnchorPane rootPane = new AnchorPane();
        rootPane.getChildren().addAll(v.getHeader(), detailsBox);
        
        // Nouvelle scène pour les détails
        Scene detailScene = new Scene(rootPane, 1350, 670);
        detailScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(detailScene);
    }
}
