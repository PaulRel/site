package Interface;

import java.io.IOException;
import customer.Customer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import products.Produit;

public class MainView extends Application {

	private Stage primaryStage;
	private HeaderView headerView;
	private String css;
	private static Customer currentCustomer;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
    	this.primaryStage=primaryStage;
    	css = this.getClass().getResource("/style.css").toExternalForm();
    	//root = new AnchorPane();
    	
       	headerView = new HeaderView(this); //utile ?
        
        showProductView(Produit.class);
        //createScrollPane();
        
        setupStage(primaryStage);
        primaryStage.show();
    }
    
    /**
     * Configure la scène principale de l'application et applique les styles CSS.
     * 
     * @param primaryStage La scène principale de l'application.
     * @throws IOException En cas d'erreur de chargement des ressources.
     */
    public void setupStage(Stage primaryStage) throws IOException{      
        Image icon = new Image (getClass().getResource("/Image/logo.jpg").toExternalForm());
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Magasin de tennis");
        primaryStage.setFullScreen(false); //A remettre vrai
    }
    
    /**
     * Crée et place la section de produits au centre de l'interface utilisateur.
     */
    public void showProductView(Class<? extends Produit> typeProduit) {
    	ProductView productSection = new ProductView(this, typeProduit);
    	AnchorPane root = productSection.getRoot();
    	root.getChildren().add(headerView.getHeader()); // Ajouter l'en-tête
    	Scene productScene = new Scene(root, 1350, 670);
    	productScene.getStylesheets().add(css);
    	primaryStage.setScene(productScene);          
    }
    
    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }
    
    /**
     * Crée un conteneur défilant (ScrollPane) pour la mise en page principale, permettant le défilement du contenu.
     */
    public static ScrollPane createScrollPane(Pane root) {
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true); // Le contenu s'adapte à la largeur de la fenêtre
        scrollPane.setPannable(true);   // Permet le défilement via la souris
        //scrollPane.getStyleClass().add("scroll-pane-style"); // Appliquer une classe CSS (facultatif)
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);  // Toujours afficher la barre de défilement verticale
        return scrollPane;
    }


    public static void main(String[] args) {
        launch(args);
    }

	public Stage getPrimaryStage() {
		return primaryStage;
	}

		/**
	     * Crée l'en-tête de l'application, qui inclut le nom du magasin, l'icône du compte et la barre de navigation.
	     * 
	     * @param primaryStage La scène principale de l'application.
	     */
	    /*public void createHeader(Stage primaryStage) {
	    	HBox topBar = new HBox();
	    	topBar.setPrefHeight(50);
	    	
	    	header = new VBox();
	    	header.setPrefHeight(100);
	    	
	    	AnchorPane.setTopAnchor(header, 10.0);
	    	AnchorPane.setLeftAnchor(header, 10.0);
	    	
	    	//Icone du magasin
	    	ImageView logo = new ImageView (new Image(getClass().getResource("/Image/logo.jpg").toExternalForm()));
	    	logo.setFitHeight(40); // Ajuster la taille de l'image
	    	logo.setFitWidth(40);
	    	
	    	//Nom du magasin
	    	Label Name = new Label("Shop");
	    	Name.setStyle("-fx-text-fill: #333; -fx-font-size: 30px; -fx-font-weight: bold;");
	    	
	        Button accountButton = createAccountButton(primaryStage);
	    		
	    	// Barre de menu
	        MenuBar menuBar = new MenuBar();
	        Menu menuVetements = new Menu("VETEMENTS");
	        Menu menuSacs = new Menu("SACS");
	        Menu menuChaussures = new Menu("CHAUSSURES");
	        
	     // Associer un événement de clic pour chaque type de produit
	        menuVetements.setOnAction(e -> createProductSection(primaryStage, Vetement.class));
	        menuSacs.setOnAction(e -> createProductSection(primaryStage, Sac.class));
	        menuChaussures.setOnAction(e -> createProductSection(primaryStage, Accessoire.class));
	        
	        menuBar.getMenus().addAll(menuVetements, menuSacs, menuChaussures);
	        
	        // Ecouteur pour ajuster la largeur de wrap en fonction de la taille de la fenêtre
	        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
	            header.setPrefWidth(newValue.doubleValue()-20);
	        });
	        
	        Region spacer = new Region();
	        HBox.setHgrow(spacer, Priority.ALWAYS);
	        
	        topBar.getChildren().addAll(logo, Name, spacer, accountButton);
	        header.getChildren().addAll(topBar, menuBar);
	        root.getChildren().add(header);
	    }
	    
	    public Button createAccountButton(Stage primaryStage){
	    	// Icône du compte à droite (bouton avec icône)
	    	ImageView accountIcon = new ImageView(new Image(getClass().getResource("/Image/accountIcon.png").toExternalForm()));
	    	accountIcon.setFitHeight(40); // Ajuster la taille de l'image
	    	accountIcon.setFitWidth(40); 
	    	
	    	Button accountButton = new Button();
	        
	    	accountButton.setGraphic(accountIcon); // Placer l'icône dans le bouton
	    	accountButton.setStyle("-fx-background-color: transparent;"); // Enlever le fond pour le bouton
	        
	    	// Gestion du clic sur le produit pour afficher les détails
	    	accountButton.setOnMouseClicked(event -> {
	    	    AuthController authController = new AuthController(primaryStage, mainScene);
	    	    if (authController.isAuthenticated()) {
	    	        System.out.println("Utilisateur authentifié");
	    	        // Effectuez ici les actions nécessaires après l'authentification
	    	    } else {
	    	        //System.out.println("Échec de l'authentification");
	    	    }
	    	   });
	    	return accountButton;
	    }*/
}