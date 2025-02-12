package Interface;

import java.util.List;

import customer.Customer.Role;
import database.SearchDAO;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import products.Shoes;
import products.Product;
import products.Clothing;

/**
 * La classe HeaderView représente l'en-tête de l'application.
 * Il contient le logo, le nom du magasin, un champ de recherche, 
 * des boutons pour l'authentification, le panier et le menu de navigation.
 */
public class HeaderView{
    private VBox header;

    /**
     * Constructeur de la classe HeaderView.
     * Initialise et affiche l'en-tête.
     * 
     * @param mainView La vue principale de l'application.
     */
    public HeaderView(MainView mainView){
        createHeader(mainView);
    }

    /**
     * Constructeur de la classe HeaderView.
     * Initialise et affiche l'en-tête.
     * 
     * @param mainView La vue principale de l'application.
     */
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
        
        // Champ de recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un produit...");
        searchField.setPrefSize(300, 40); 
        searchField.setOnAction(event -> performSearch(searchField.getText(), mainView)); 
        
        // Bouton de recherche
        Button searchButton = new Button();
        ImageView searchIcon = new ImageView(new Image(getClass().getResource("/Image/Icons/searchIcon.png").toExternalForm()));
        searchIcon.setFitHeight(30);
        searchIcon.setFitWidth(30);
        searchButton.setGraphic(searchIcon);
        searchButton.setStyle("-fx-background-color:transparent; -fx-border-color: #e0e0e0; -fx-pref-width: 50px; -fx-pref-height: 50px; -fx-padding: 0 0 0 0; -fx-border-radius: 0;");
        searchButton.setOnAction(event -> performSearch(searchField.getText(), mainView));
        
        // Lien pour la recherche avancée
        Hyperlink hyperlink = new Hyperlink("Recherche avancée");
        hyperlink.setOnAction(event -> new SearchAdvancedView(mainView));

        topBar.getChildren().addAll(logo, shopName, leftSpacer, searchField, searchButton, hyperlink, rightSpacer, accountButton, cartButton);
        
        
        // Barre de menu
        HBox menuBar = new HBox();
        menuBar.setId("menuBar");
        
        Button clothingButton = new Button("VETEMENTS");
        Button shoesButton = new Button("CHAUSSURES");

        // Associer un événement de clic pour chaque item de menu
        clothingButton.setOnAction(e ->{ 
        	mainView.showProductView(Clothing.class, null); 
        	clothingButton.setStyle("-fx-border-color: #00ffe8;; -fx-background-color: #dedede; -fx-border-color: #00ffe8; -fx-border-width: 0px 0px 3px 0px;");
        	shoesButton.setStyle("-fx-border-width: 0px 0px 3px 0px; -fx-border-width: 0px 0px 0px 0px;");
        });
        
        shoesButton.setOnAction(e -> {
        	shoesButton.setStyle("-fx-border-color: #00ffe8;; -fx-background-color: #dedede; -fx-border-color: #00ffe8; -fx-border-width: 0px 0px 3px 0px;");
        	clothingButton.setStyle("-fx-border-width: 0px 0px 3px 0px; -fx-border-width: 0px 0px 0px 0px;");
        	mainView.showProductView(Shoes.class, null);
        });
        
        clothingButton.setId("clothingButton");
        shoesButton.setId("shoesButton");
        
        menuBar.getChildren().addAll(clothingButton, shoesButton);
        
        header.getChildren().addAll(topBar, menuBar);
        header.setPrefWidth(mainView.getPrimaryStage().getWidth() - 20);
        
        AnchorPane.setTopAnchor(header, 10.0);
    }
    
    /**
     * Effectue une recherche de produits en fonction du terme entré par l'utilisateur.
     * 
     * @param searchTerm Le terme de recherche saisi par l'utilisateur.
     * @param mainView   La vue principale où les résultats seront affichés.
     */
    private void performSearch(String searchTerm, MainView mainView) {
        SearchDAO searchDAO = new SearchDAO();
        List<Product> products = searchDAO.search(searchTerm);
        mainView.showProductView(Product.class, products);
    }

    /**
     * Retourne l'en-tête de l'application.
     * 
     * @return Un VBox contenant l'en-tête.
     */
    public VBox getHeader() {
        return header;
    }
}

