package Interface;

import java.util.List;

import customer.Customer.Role;
import database.SearchDAO;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import products.Chaussures;
import products.Product;
import products.Sac;
import products.Vetement;

public class HeaderView{
    private VBox header;

    public HeaderView(MainView mainView){
        createHeader(mainView);
    }

    private void createHeader(MainView mainView) {
        header = new VBox();
        
        HBox topBar = new HBox();
        topBar.setStyle("-fx-pref-height: 50; -fx-alignment: center; -fx-padding: 0 0 0 20;");

        // Logo du magasin
        ImageView logo = new ImageView(new Image(getClass().getResource("/Image/logo.jpg").toExternalForm()));
        logo.setFitHeight(40);
        logo.setFitWidth(40);

        // Nom du magasin
        Label shopName = new Label("TennisShop");
        shopName.setStyle("-fx-text-fill: #333; -fx-font-size: 30px; -fx-font-weight: bold; -fx-cursor: hand;");
        shopName.setOnMouseClicked(e -> mainView.showProductView(Product.class, null));
        
        // Bouton du compte
        ImageView accountIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/accountIcon.png").toExternalForm()));
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
    			if (MainView.getCurrentCustomer().getRole()==Role.CUSTOMER)
        			new AccountView(mainView);
        		else new AdminView(mainView);
    		}
    	});
    	
    	// Bouton du cart
        ImageView cartIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/cartIcon.jpg").toExternalForm()));
        cartIcon.setFitHeight(40);
        cartIcon.setFitWidth(40);
        Button cartButton = new Button();
        cartButton.setGraphic(cartIcon);
        cartButton.setStyle("-fx-background-color: transparent;");
        
        // Gestion du clic pour afficher CartView
    	cartButton.setOnMouseClicked(event -> new CartView(mainView));
        
        // Ecouteur pour ajuster la largeur de wrap en fonction de la taille de la fenêtre
        mainView.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) -> {
            header.setPrefWidth(newValue.doubleValue()-20);
        });

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un produit...");
        searchField.setPrefSize(300, 40); 
        searchField.setOnAction(event -> performSearch(searchField.getText(), mainView)); 
        
        Button searchButton = new Button();
        ImageView searchIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/searchIcon.png").toExternalForm()));
        searchIcon.setFitHeight(30);
        searchIcon.setFitWidth(30);
        searchButton.setGraphic(searchIcon);
        searchButton.setStyle("-fx-background-color:transparent; -fx-border-color: #e0e0e0; -fx-pref-width: 50px; -fx-pref-height: 50px; -fx-padding: 0 0 0 0; -fx-border-radius: 0;");
        searchButton.setOnAction(event -> performSearch(searchField.getText(), mainView));
        
        Hyperlink hyperlink = new Hyperlink("Recherche avancée");
        hyperlink.setOnAction(event -> new SearchAdvancedView(mainView));

        topBar.getChildren().addAll(logo, shopName, leftSpacer, searchField, searchButton, hyperlink, rightSpacer, accountButton, cartButton);
        
        
        // Barre de menu
        HBox menuBar = new HBox();
        menuBar.setId("menuBar");
        
        Button vetementsButton = new Button("VETEMENTS");
        Button chaussuresButton = new Button("CHAUSSURES");

        // Associer un événement de clic pour chaque item de menu
        vetementsButton.setOnAction(e ->{ 
        	mainView.showProductView(Vetement.class, null); 
        	vetementsButton.setStyle("-fx-border-color: #00ffe8;; -fx-background-color: #dedede; -fx-border-color: #00ffe8; -fx-border-width: 0px 0px 3px 0px;");
        	chaussuresButton.setStyle("-fx-border-width: 0px 0px 3px 0px; -fx-border-width: 0px 0px 0px 0px;");
        });
        
        chaussuresButton.setOnAction(e -> {
        	chaussuresButton.setStyle("-fx-border-color: #00ffe8;; -fx-background-color: #dedede; -fx-border-color: #00ffe8; -fx-border-width: 0px 0px 3px 0px;");
        	vetementsButton.setStyle("-fx-border-width: 0px 0px 3px 0px; -fx-border-width: 0px 0px 0px 0px;");
        	mainView.showProductView(Chaussures.class, null);
        });
        
        vetementsButton.setId("vetementsButton");
        chaussuresButton.setId("chaussuresButton");
        
        menuBar.getChildren().addAll(vetementsButton, chaussuresButton);
        
        header.getChildren().addAll(topBar, menuBar);
        header.setPrefWidth(mainView.getPrimaryStage().getWidth() - 20);
        
        AnchorPane.setTopAnchor(header, 10.0);
    }
    
    private void performSearch(String searchTerm, MainView mainView) {
        SearchDAO searchDAO = new SearchDAO();
        List<Product> products = searchDAO.search(searchTerm);
        mainView.showProductView(Product.class, products);
    }

    public VBox getHeader() {
        return header;
    }
}

