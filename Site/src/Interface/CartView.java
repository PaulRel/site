package Interface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import products.Produit;

public class CartView {
	
	public CartView(MainView mainView, Stage primaryStage) {
		Label emptyCartLabel = new Label("Votre panier est vide");
		Button continueButton = new Button("Continuer vos achats");
		continueButton.setOnAction(e -> mainView.showProductView(Produit.class));
				
		// Mise en page principale
        //VBox main = new VBox(emptyCartLabel, continueButton);
        //main.setPadding(new Insets(10));
        //main.setMaxSize(700, 400);
        //main.setAlignment(Pos.CENTER);
        //main.setStyle("-fx-background-color: white");
		
		VBox root = new VBox(20, emptyCartLabel, continueButton);
	    AnchorPane.setTopAnchor(root, 116.0);
	    root.setPadding(new Insets(30));
	    root.setPrefSize(1350, 550);
	    root.setStyle("-fx-background-color: #EEEEEE");
	    root.setAlignment(Pos.TOP_CENTER);
	    primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {root.setPrefWidth(newValue.doubleValue());}); // Ajuste la largeur
	    primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {root.setPrefHeight(newValue.doubleValue()-116);}); // Ajuste la hauteur
	    
	    AnchorPane rootPane = new AnchorPane();
	    HeaderView v = new HeaderView(mainView, primaryStage);      
	    rootPane.getChildren().addAll(v.getHeader(), root);
	    
	    Scene createAccountScene = new Scene(rootPane, 1350, 670);
	               
	    String css = this.getClass().getResource("/style.css").toExternalForm();        
	    createAccountScene.getStylesheets().add(css);
	        
	    primaryStage.setScene(createAccountScene);
	}

}
