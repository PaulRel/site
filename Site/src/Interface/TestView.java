package Interface;

import java.io.IOException;
import java.util.List;

import Products.Produit;
import database.ProduitDAO;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
	private AnchorPane root;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
    	initializeRootLayout();
        createHeader(primaryStage);
        createProductSection(primaryStage);
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
    public void createHeader(Stage primaryStage) {
    	Label Name = new Label("Shop");
    	Name.setStyle("-fx-text-fill: #333; -fx-font-size: 30px; -fx-font-weight: bold;");
    	//Placer le nom en haut à gauche
        AnchorPane.setTopAnchor(Name, 0.0);
        AnchorPane.setLeftAnchor(Name, 10.0);
        
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
        
    	// Gestion du clic sur le produit pour afficher les détails
        accountButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                loginPage(primaryStage);
            }
        }); 	
    	
    	// Barre de menu
        MenuBar menuBar = new MenuBar();
        Menu menuVetements = new Menu("Vêtements");
        Menu menuSacs = new Menu("Sacs");
        Menu menuAccessoires = new Menu("Accessoires");
        menuBar.getMenus().addAll(menuVetements, menuSacs, menuAccessoires);
        
        // Placer la barre de menu en haut
        AnchorPane.setTopAnchor(menuBar, 50.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);
        
        root.getChildren().addAll(Name, accountButton, menuBar);
    }
    
    /**
     * Crée et place la section de produits au centre de l'interface utilisateur.
     * 
     * @param primaryStage La scène principale de l'application.
     */
    public void createProductSection(Stage primaryStage) {
        VBox sectionProduits = new VBox();
        sectionProduits.getChildren().add(displayProducts(primaryStage));
        
        AnchorPane.setTopAnchor(sectionProduits, 120.0);
        AnchorPane.setLeftAnchor(sectionProduits, 10.0);
        AnchorPane.setRightAnchor(sectionProduits, 200.0); // Ajuster la largeur du côté droit pour les filtres
        
        root.getChildren().add(sectionProduits);
    }
    
    /**
     * Crée une boîte latérale contenant des filtres de produits.
     */
    private void createFilterBox() {
        VBox filtreBox = new VBox();
        AnchorPane.setTopAnchor(filtreBox, 120.0);
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
        
        root.getChildren().add(filtreBox);
    }
    
    /**
     * Crée un conteneur défilant (ScrollPane) pour la mise en page principale, permettant le défilement du contenu.
     */
    public void createScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true); // Le contenu s'adapte à la largeur de la fenêtre
        scrollPane.setPannable(true);   // Permet le défilement via la souris
    }
    
    /**
     * Configure la scène principale de l'application et applique les styles CSS.
     * 
     * @param primaryStage La scène principale de l'application.
     * @throws IOException En cas d'erreur de chargement des ressources.
     */
    public void setupMainScene(Stage primaryStage) throws IOException{
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
    }
    
    /**
     * Affiche une grille de produits dans une interface utilisateur de type GridPane.
     * 
     * @param primaryStage La scène principale de l'application.
     * @return Le GridPane contenant tous les produits.
     */
    public GridPane displayProducts(Stage primaryStage) {
        GridPane produitsGrid = new GridPane();
        produitsGrid.setPadding(new Insets(10, 10, 10, 10));
        produitsGrid.setHgap(10);
        produitsGrid.setVgap(10);
        
        // Récupérer les produits depuis la base de données
        ProduitDAO produitDAO = new ProduitDAO();
        List<Produit> produits = produitDAO.getAllProduits();
        int colonne = 0;
        int ligne = 0;
        
        // Boucle pour ajouter chaque produit à la grille
        for (Produit produit : produits) {
        	VBox produitBox = createProductBox(primaryStage, produit);
            
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
    
    public void loginPage(Stage primaryStage) {
    	
    }

    public static void main(String[] args) {
        launch(args);
    }
}