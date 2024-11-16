package Interface;

import java.util.List;

import database.ProduitDAO;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import products.Produit;

public class ProductView {
	
	private AnchorPane root;
	
	public ProductView(MainView mainView, Stage primaryStage, Class<? extends Produit> typeProduit) {
		root = new AnchorPane();
		VBox sectionProduits = new VBox();
		sectionProduits.getChildren().clear();
        sectionProduits.getChildren().add(displayProducts(mainView, primaryStage, typeProduit));
        
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
   public ScrollPane displayProducts(MainView mainView, Stage primaryStage, Class<? extends Produit> typeProduit) {
       FlowPane produitsGrid = new FlowPane();
       produitsGrid.setPadding(new Insets(10, 10, 10, 10));
       produitsGrid.setHgap(10);
       produitsGrid.setVgap(10);
       produitsGrid.setPrefWrapLength(primaryStage.getWidth()); // Ajuste automatiquement avec la largeur
       ProduitDAO produitDAO = new ProduitDAO();  // Récupérer les produits depuis la base de données
       List<Produit> produits = produitDAO.getAllProduits();
       
       produits.stream()
       .filter(typeProduit::isInstance) // Filtrer les produits par type
       .forEach(produit -> {
       	// TESTS :  System.out.println(typeProduit); System.out.println("Nom de la classe : " + produit.getClass().getSimpleName());
       	VBox produitBox = createProductBox(mainView, produit);
       	produitsGrid.getChildren().add(produitBox);
       });
       	
    // Ecouteur pour ajuster la largeur de wrap en fonction de la taille de la fenêtre
       primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
           produitsGrid.setPrefWrapLength(newValue.doubleValue());
       });
       
    // Encapsuler le FlowPane dans un ScrollPane
       ScrollPane scrollPane = new ScrollPane(produitsGrid);
       scrollPane.setFitToWidth(true); // Adapter la largeur du contenu à celle de la fenêtre
       scrollPane.setPannable(true);   // Activer le défilement pour une expérience fluide
       scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Toujours afficher la barre de défilement verticale
       scrollPane.setPrefViewportHeight(510);
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
   private VBox createProductBox(MainView mainView, Produit produit) {
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
       produitBox.setOnMouseClicked(event -> mainView.showDetailsProductView(Produit.class));
       
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

public AnchorPane getRoot() {
	return root;
}

public void setRoot(AnchorPane root) {
	this.root = root;
}

}
