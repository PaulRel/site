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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import products.Produit;

public class ProductView {
	
	private AnchorPane root;
	
	public ProductView(MainView mainView, Class<? extends Produit> typeProduit) {
		root = new AnchorPane();
		HBox sectionProduits = new HBox();
		sectionProduits.setPadding(new Insets(10, 20, 10, 20)); // Espace  Haut, Droite>, Bas, Gauche<  de l'AnchorPane
		sectionProduits.setSpacing(50); // Espacement entre filtre et grid
		sectionProduits.getChildren().clear();
        sectionProduits.getChildren().addAll(createFilterBox(), displayProducts(mainView, typeProduit));
        
        AnchorPane.setTopAnchor(sectionProduits, 150.0);
        AnchorPane.setLeftAnchor(sectionProduits, 10.0);
        root.getChildren().add(sectionProduits);
	}
	
	/**
    * Affiche une grille de produits dans une interface utilisateur de type GridPane.
    * 
    * @param primaryStage La scène principale de l'application.
    * @return Le GridPane contenant tous les produits.
    */
   public ScrollPane displayProducts(MainView mainView, Class<? extends Produit> typeProduit) {
       FlowPane produitsGrid = new FlowPane();
       produitsGrid.setPadding(new Insets(10));
       produitsGrid.setHgap(10);
       produitsGrid.setVgap(10);
       produitsGrid.setPrefWrapLength(mainView.getPrimaryStage().getWidth()-300);
       ProduitDAO produitDAO = new ProduitDAO();  // Récupérer les produits depuis la base de données
       List<Produit> produits = produitDAO.getAllProduits();
       
       produits.stream()
       .filter(typeProduit::isInstance) // Filtrer les produits par type
       .forEach(produit -> {
       	// TESTS :  System.out.println(typeProduit); System.out.println("Nom de la classe : " + produit.getClass().getSimpleName());
       	VBox produitBox = createProductBox(mainView, produit);
       	produitsGrid.getChildren().add(produitBox);
       });
       	
       // Ecouteur pour ajuster automatiquement avec la largeur de la fenêtre
       mainView.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) -> {
           produitsGrid.setPrefWrapLength(newValue.doubleValue()-300);
       });
       
       // Encapsuler le FlowPane dans un ScrollPane
       ScrollPane scrollPane = MainView.createScrollPane(produitsGrid);
       scrollPane.setPrefViewportHeight(510);
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
       produitBox.setOnMouseClicked(event -> new ProductDetailsView(mainView, produit));
       
       return produitBox;
    }
   
   /**
    * Crée une boîte latérale contenant des filtres de produits.
    */
   private VBox createFilterBox() {
       VBox filtreBox = new VBox();

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

       return filtreBox;
   }

public AnchorPane getRoot() {
	return root;
}

public void setRoot(AnchorPane root) {
	this.root = root;
}

}
