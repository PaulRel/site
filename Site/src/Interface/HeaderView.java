package Interface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HeaderView {
    private VBox header;

    public HeaderView(Stage primaryStage, Scene mainScene) {
        createHeader(primaryStage, mainScene);
    }

    private void createHeader(Stage primaryStage, Scene mainScene) {
        HBox topBar = new HBox();
        topBar.setPrefHeight(50);

        header = new VBox();
        header.setPrefHeight(100);

        // Logo du magasin
        ImageView logo = new ImageView(new Image(getClass().getResource("/Image/logo.jpg").toExternalForm()));
        logo.setFitHeight(40);
        logo.setFitWidth(40);
        

        // Nom du magasin
        Label shopName = new Label("TennisShop");
        shopName.setStyle("-fx-text-fill: #333; -fx-font-size: 30px; -fx-font-weight: bold;");
        shopName.setOnMouseClicked(e -> primaryStage.setScene(mainScene));
        
        // Bouton du compte
        ImageView accountIcon = new ImageView(new Image(getClass().getResource("/Image/accountIcon.png").toExternalForm()));
        accountIcon.setFitHeight(40);
        accountIcon.setFitWidth(40);
        Button accountButton = new Button();
        accountButton.setGraphic(accountIcon);
        accountButton.setStyle("-fx-background-color: transparent;");
        
     // Gestion du clic pour afficher LoginPage
    	accountButton.setOnMouseClicked(event -> {
    	    AuthController authController = new AuthController(primaryStage, mainScene);
    	    if (authController.isAuthenticated()) {
    	        System.out.println("Utilisateur authentifié");
    	        // Effectuez ici les actions nécessaires après l'authentification
    	    } else {
    	        //System.out.println("Échec de l'authentification");
    	    }
    	   });	
        
        // Barre de menu
        MenuBar menuBar = new MenuBar(new Menu("VETEMENTS"), new Menu("SACS"), new Menu("CHAUSSURES"));
        
        
        // Ecouteur pour ajuster la largeur de wrap en fonction de la taille de la fenêtre
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            header.setPrefWidth(newValue.doubleValue()-20);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(logo, shopName, spacer, accountButton);
        header.getChildren().addAll(topBar, menuBar);
        header.setPrefWidth(primaryStage.getWidth() - 20);
        
        AnchorPane.setTopAnchor(header, 10.0);
        AnchorPane.setLeftAnchor(header, 10.0);
    }

    public VBox getHeader() {
        return header;
    }
}

