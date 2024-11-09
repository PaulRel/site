package Interface;

import java.io.IOException;
import java.util.List;

import Products.Accessoire;
import Products.Produit;
import Products.Sac;
import Products.Vetement;
import database.ProduitDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {

	private Scene mainScene;
	private AnchorPane root;
	private VBox header;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
    	initializeRootLayout();
    	//createHeader(primaryStage);
        createProductSection(primaryStage, null);
        createFilterBox();
        createScrollPane();        
        setupMainScene(primaryStage);        
        primaryStage.show();
    }
    
    /** 
     * Création AnchorPane root
     */
    public void initializeRootLayout() {
        root = new AnchorPane();
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
    }
    
    /**
     * Crée et place la section de produits au centre de l'interface utilisateur.
     * 
     * @param primaryStage La scène principale de l'application.
     */
    public void createProductSection(Stage primaryStage, Class<? extends Produit> typeProduit) {
        VBox sectionProduits = new VBox();
        sectionProduits.getChildren().add(displayProducts(primaryStage, typeProduit));
        
        AnchorPane.setTopAnchor(sectionProduits, 150.0);
        AnchorPane.setLeftAnchor(sectionProduits, 10.0);
        AnchorPane.setRightAnchor(sectionProduits, 200.0); // Ajuster la largeur du côté droit pour les filtres
        
        root.getChildren().add(sectionProduits);
    }
    
    /**
     * Affiche une grille de produits dans une interface utilisateur de type GridPane.
     * 
     * @param primaryStage La scène principale de l'application.
     * @return Le GridPane contenant tous les produits.
     */
    public ScrollPane displayProducts(Stage primaryStage, Class<? extends Produit> typeProduit) {
        FlowPane produitsGrid = new FlowPane();
        produitsGrid.setPadding(new Insets(10, 10, 10, 10));
        produitsGrid.setHgap(10);
        produitsGrid.setVgap(10);
        produitsGrid.setPrefWrapLength(primaryStage.getWidth()); // Ajuste automatiquement avec la largeur
        
        // Récupérer les produits depuis la base de données
        ProduitDAO produitDAO = new ProduitDAO();
        List<Produit> produits = produitDAO.getAllProduits();
        
        for (Produit produit : produits) {
        	VBox produitBox = createProductBox(primaryStage, produit);
            produitsGrid.getChildren().add(produitBox);
        }
        
     // Ecouteur pour ajuster la largeur de wrap en fonction de la taille de la fenêtre
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            produitsGrid.setPrefWrapLength(newValue.doubleValue());
        });
        
     // Encapsuler le FlowPane dans un ScrollPane
        ScrollPane scrollPane = new ScrollPane(produitsGrid);
        scrollPane.setFitToWidth(true); // Adapter la largeur du contenu à celle de la fenêtre
        scrollPane.setPannable(true);   // Activer le défilement pour une expérience fluide
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Toujours afficher la barre de défilement verticale
        scrollPane.setPrefViewportHeight(550);
        //scrollPane.getStyleClass().add("scroll-pane-style"); // Appliquer une classe CSS        
        return scrollPane;
    }   
    
    /**
     * Crée un conteneur VBox pour afficher chaque produit avec ses informations.
     * 
     * @param primaryStage La scène principale de l'application.
     * @param produit      Le produit à afficher.
     * @return Un VBox contenant l'image, le nom et le prix du produit.
     */
    private VBox createProductBox(Stage primaryStage, Produit produit) {
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
        produitBox.setOnMouseClicked(event -> showProductDetails(primaryStage, produit));
        
        return produitBox;
     }
    
    /**
     * Crée une boîte latérale contenant des filtres de produits.
     */
    private void createFilterBox() {
        VBox filtreBox = new VBox();
        AnchorPane.setTopAnchor(filtreBox, 150.0);
        AnchorPane.setRightAnchor(filtreBox, 10.0);
        
        // Ajout des filtres de taille et de sexe
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
        
       // Vider les produits existants et ajouter la nouvelle section
        //root.getChildren().clear();
        root.getChildren().add(filtreBox);
    }
    
    /**
     * Crée un conteneur défilant (ScrollPane) pour la mise en page principale, permettant le défilement du contenu.
     */
    public void createScrollPane() {
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true); // Le contenu s'adapte à la largeur de la fenêtre
        scrollPane.setPannable(true);   // Permet le défilement via la souris
        scrollPane.getStyleClass().add("scroll-pane-style"); // Appliquer une classe CSS (facultatif)
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }
    
    /**
     * Configure la scène principale de l'application et applique les styles CSS.
     * 
     * @param primaryStage La scène principale de l'application.
     * @throws IOException En cas d'erreur de chargement des ressources.
     */
    public void setupMainScene(Stage primaryStage) throws IOException{
        mainScene = new Scene(root, 1350, 670);
        //scene.getStylesheets().add("file:/Site/resources/style.css");
        String css = this.getClass().getResource("/style.css").toExternalForm();
        mainScene.getStylesheets().add(css);
        Image icon = new Image (getClass().getResource("/Image/logo.jpg").toExternalForm());
        
        HeaderView v=new HeaderView(primaryStage, mainScene);
        root.getChildren().add(v.getHeader());
        
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Magasin de tennis");
        primaryStage.setScene(mainScene);
        primaryStage.setFullScreen(false); //A remettre vrai
    }
    
    /**
     * Affiche les détails d'un produit spécifique dans une nouvelle scène.
     * 
     * @param primaryStage La scène principale de l'application.
     * @param produit      Le produit dont les détails sont à afficher.
     */
    public void showProductDetails(Stage primaryStage, Produit produit) {
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
        
        // Bouton pour revenir à la scène principale
        Button retourButton = new Button("Retour");
        retourButton.setOnAction(e -> primaryStage.setScene(mainScene));  // revenir à la scène principale

        // Ajouter les éléments à la boîte de détails
        detailsBox.getChildren().addAll(nomLabel, prixLabel, imageView, retourButton);
        
        // Nouvelle scène pour les détails
        Scene detailScene = new Scene(detailsBox, 800, 600);
        primaryStage.setScene(detailScene);
    }

    public static void main(String[] args) {
        launch(args);
    }

	public Scene getMainScene() {
		return mainScene;
	}
}