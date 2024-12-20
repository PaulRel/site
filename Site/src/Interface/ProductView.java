package Interface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import products.Chaussures;
import products.Product;
import products.Vetement;
import products.ProductWithSize;

public class ProductView {
	
	private AnchorPane root;
	private FlowPane produitsGrid;
	private ProduitDAO produitDAO;
	List<Product> actualProducts;
	private Set<String> sizeSelected, sexSelected;
	private CheckBox tailleS, tailleM, tailleL, sexeHomme, sexeFemme, enfant;
	
	public ProductView(MainView mainView, Class<? extends Product> typeProduit) {
		root = new AnchorPane();
		HBox sectionProduits = new HBox();
		sectionProduits.setPadding(new Insets(10, 20, 10, 20)); // Espace  Haut, Droite>, Bas, Gauche<  de l'AnchorPane
		sectionProduits.setSpacing(50); // Espacement entre filtre et grid
		sectionProduits.getChildren().clear();
        sectionProduits.getChildren().addAll(createFilterBox(mainView), displayProducts(mainView, typeProduit));
        
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
   public ScrollPane displayProducts(MainView mainView, Class<? extends Product> typeProduit) {
       produitsGrid = new FlowPane();
       produitsGrid.setPadding(new Insets(10));
       produitsGrid.setHgap(10);
       produitsGrid.setVgap(10);
       produitsGrid.setPrefWrapLength(mainView.getPrimaryStage().getWidth()-300);
       
       produitDAO = new ProduitDAO();  // Récupérer les produits depuis la base de données
       List<Product> products = produitDAO.getAllProduits();
       actualProducts = new ArrayList<Product>();
       
       products.stream()
       .filter(typeProduit::isInstance) // Filtrer les produits par type
       .forEach(produit -> {
    	   // TESTS :  System.out.println(typeProduit); System.out.println("Nom de la classe : " + produit.getClass().getSimpleName());
    	   actualProducts.add(produit);
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
    * @param product      Le produit à afficher.
    * @return Un VBox contenant l'image, le nom et le prix du produit.
    */
   private VBox createProductBox(MainView mainView, Product product) {
   	// Création des composants pour chaque produit
       ImageView imageView = new ImageView(new Image(getClass().getResource(product.getImagePath()).toExternalForm()));
       imageView.setFitHeight(150);
       imageView.setFitWidth(150);

       Label nomProduit = new Label(product.getNom());
       nomProduit.getStyleClass().add("nom-produit"); // Appliquer la classe CSS
       Label prixProduit = new Label("Prix : " + product.getPrice() + "€");
       prixProduit.getStyleClass().add("prix-produit"); // Appliquer la classe CSS

       VBox produitBox = new VBox();
       produitBox.getStyleClass().add("produit-box"); // Appliquer la classe CSS
       produitBox.getChildren().addAll(imageView, nomProduit, prixProduit);
       
       // Gestion du clic sur le produit pour afficher les détails
       produitBox.setOnMouseClicked(event -> new ProductDetailsView(mainView, product));
       
       return produitBox;
    }
   
   /**
    * Crée une boîte latérale contenant des filtres de produits.
    */
   private VBox createFilterBox(MainView mainView) {
       VBox filtreBox = new VBox();

       Label filtreTaille = new Label("Tailles");
       tailleS = new CheckBox("Taille S");
       tailleM = new CheckBox("Taille M");
       tailleL = new CheckBox("Taille L");
       filtreBox.getChildren().addAll(filtreTaille, tailleS, tailleM, tailleL);
       
       Label filtreSexe = new Label("Sexe");
       sexeHomme = new CheckBox("Homme");
       sexeFemme = new CheckBox("Femme");
       enfant = new CheckBox("Enfant");
       filtreBox.getChildren().addAll(filtreSexe, sexeHomme, sexeFemme, enfant);
       
       tailleS.setOnAction(event -> applyFilters(mainView));
       tailleM.setOnAction(event -> applyFilters(mainView));
       tailleL.setOnAction(event -> applyFilters(mainView));
       sexeHomme.setOnAction(event -> applyFilters(mainView));
       sexeFemme.setOnAction(event -> applyFilters(mainView));
       enfant.setOnAction(event -> applyFilters(mainView));
       
       return filtreBox;
   }
   
   private void applyFilters(MainView mainView) {
	   	// Collecte des filtres sélectionnés
	    sizeSelected = new HashSet<>();
	    if (tailleS.isSelected()) sizeSelected.add("S");
	    if (tailleM.isSelected()) sizeSelected.add("M");
	    if (tailleL.isSelected()) sizeSelected.add("L");

	    sexSelected = new HashSet<>();
	    if (sexeHomme.isSelected()) sexSelected.add("Homme");
	    if (sexeFemme.isSelected()) sexSelected.add("Femme");
	    if (enfant.isSelected()) sexSelected.add("Enfant");
	    
	    updateProductDisplay(mainView);
	}

   private boolean filterByAttributes(Product product) {
	    if (product instanceof Chaussures || product instanceof Vetement) {
	        String sex =  ((ProductWithSize) product).getGenre();

	        boolean matchSize = sizeSelected.isEmpty() || ((ProductWithSize) product).getTailleStock().keySet().stream().anyMatch(sizeSelected::contains);
	        boolean matchSex = sexSelected.isEmpty() || sexSelected.contains(sex);

	        return matchSize && matchSex;
	    }
	    return true;
   }
   
   private void updateProductDisplay(MainView mainView) {
	    produitsGrid.getChildren().clear(); // Nettoie l'affichage actuel
	    actualProducts.stream()
	       .filter(this::filterByAttributes)
	       .forEach(produit -> {
	        VBox produitBox = createProductBox(mainView, produit); // Crée une box pour chaque produit
	        produitsGrid.getChildren().add(produitBox); // Ajoute la box au GridPane
	    });
	}

public AnchorPane getRoot() {
	return root;
}

public void setRoot(AnchorPane root) {
	this.root = root;
}

}
