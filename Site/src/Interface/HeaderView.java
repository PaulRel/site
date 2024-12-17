package Interface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import products.Chaussures;
import products.Produit;
import products.Sac;
import products.Vetement;

public class HeaderView{
    private VBox header;

    public HeaderView(MainView mainView){
        createHeader(mainView);
    }

    private void createHeader(MainView mainView) {   	
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
        shopName.setOnMouseClicked(e -> mainView.showProductView(Produit.class));
        
        // Bouton du compte
        ImageView accountIcon = new ImageView(new Image(getClass().getResource("/Image/accountIcon.png").toExternalForm()));
        accountIcon.setFitHeight(40);
        accountIcon.setFitWidth(40);
        Button accountButton = new Button();
        accountButton.setGraphic(accountIcon);
        accountButton.setStyle("-fx-background-color: transparent;");
        
        // Gestion du clic pour afficher LoginPage
    	accountButton.setOnMouseClicked(event -> {
    		if (MainView.getCurrentCustomer()==null){
    			new AuthentificationView(mainView);
    		}
    		else {
    			new AccountView(mainView);
    		}
    	});
    	
    	// Bouton du cart
        ImageView cartIcon = new ImageView(new Image(getClass().getResource("/Image/cartIcon.jpg").toExternalForm()));
        cartIcon.setFitHeight(40);
        cartIcon.setFitWidth(40);
        Button cartButton = new Button();
        cartButton.setGraphic(cartIcon);
        cartButton.setStyle("-fx-background-color: transparent;");
        
        // Gestion du clic pour afficher CartPage
    	cartButton.setOnMouseClicked(event -> new CartView(mainView));
                          
        // Barre de menu
    	MenuBar menuBar = new MenuBar();
        Menu menuVetements = new Menu("VETEMENTS");
        Menu menuSacs = new Menu("SACS");
        Menu menuChaussures = new Menu("CHAUSSURES");
        
        MenuItem vetementsItem = new MenuItem("VETEMENTS");
        MenuItem sacsItem = new MenuItem("SACS");
        MenuItem chaussuresItem = new MenuItem("CHAUSSURES");

        // Associer un événement de clic pour chaque item de menu
        vetementsItem.setOnAction(e -> mainView.showProductView(Vetement.class));
        sacsItem.setOnAction(e -> mainView.showProductView(Sac.class));
        chaussuresItem.setOnAction(e -> mainView.showProductView(Chaussures.class));
        
        // Ajouter les items aux menus
        menuVetements.getItems().add(vetementsItem);
        menuSacs.getItems().add(sacsItem);
        menuChaussures.getItems().add(chaussuresItem);
        
        menuBar.getMenus().addAll(menuVetements, menuSacs, menuChaussures);
        
        // Ecouteur pour ajuster la largeur de wrap en fonction de la taille de la fenêtre
        mainView.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) -> {
            header.setPrefWidth(newValue.doubleValue()-20);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(logo, shopName, spacer, accountButton, cartButton);
        header.getChildren().addAll(topBar, menuBar);
        header.setPrefWidth(mainView.getPrimaryStage().getWidth() - 20);
        
        AnchorPane.setTopAnchor(header, 10.0);
        AnchorPane.setLeftAnchor(header, 10.0);
    }

    public VBox getHeader() {
        return header;
    }
}

