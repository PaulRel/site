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
import javafx.scene.control.Slider;
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
	private Set<String> sizeSelected, genderSelected, brandSelected, clothingTypeSelected;
	private CheckBox sizeXS, sizeS, sizeM, sizeL, size36, size37, size38, size39, male, female, child, marqueAsics, marqueBabolat, marqueAdidas, tankTop, sweat, shorts, tshirt, dress;
	private double maxPrice = 200.0;
	
	public ProductView(MainView mainView, Class<? extends Product> typeProduit, List<Product> products) {
		root = new AnchorPane();
		//HBox sectionProduits = new HBox();
		//sectionProduits.setPadding(new Insets(10, 0, 0, 20)); // Espace  Haut, Droite>, Bas, Gauche<  de l'AnchorPane
		//sectionProduits.setSpacing(50); // Espacement entre filtre et grid
		//sectionProduits.getChildren().clear();
		//sectionProduits.setStyle("-fx-background-color: RED; -fx-pref-width: 1350px;");
        //sectionProduits.getChildren().addAll(MainView.createScrollPane(createFilterBox(mainView)), displayProducts(mainView, typeProduit, products));
        
        //AnchorPane.setTopAnchor(sectionProduits, 150.0);
        //AnchorPane.setLeftAnchor(sectionProduits, 10.0);
        //AnchorPane.setBottomAnchor(sectionProduits, 0.0);
        //root.getChildren().add(sectionProduits);
        root.getChildren().addAll(MainView.createScrollPane(createFilterBox(mainView)), displayProducts(mainView, typeProduit, products));
	}
	
	/**
    * Affiche une grille de produits dans une interface utilisateur de type GridPane.
    * 
    * @param primaryStage La scène principale de l'application.
    * @return Le GridPane contenant tous les produits.
    */
   public ScrollPane displayProducts(MainView mainView, Class<? extends Product> typeProduit, List<Product> products) {
       produitsGrid = new FlowPane();
       produitsGrid.setPadding(new Insets(20));
       produitsGrid.setHgap(10); //espace horizontal entre les produits
       produitsGrid.setVgap(10);
       produitsGrid.setPrefSize(900, 800);
       //produitsGrid.setPrefWrapLength(mainView.getPrimaryStage().getWidth()-310);
       
       produitDAO = new ProduitDAO();  // Récupérer les produits depuis la base de données
       if (products == null) {
    	   products = produitDAO.getAllProduits();
       }
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
       //mainView.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) -> {
           //produitsGrid.setPrefWrapLength(newValue.doubleValue()-350);
       //});
       
       // Encapsuler le FlowPane dans un ScrollPane
       ScrollPane scrollPane = MainView.createScrollPane(produitsGrid);
       AnchorPane.setLeftAnchor(scrollPane, 220.0);
       AnchorPane.setRightAnchor(scrollPane, 0.0); // S'assurer qu'il occupe tout l'espace horizontal
       AnchorPane.setBottomAnchor(scrollPane, 0.0); // S'assurer qu'il occupe tout l'espace vertical
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
       VBox filterBox = new VBox();
       filterBox.setPrefWidth(200);
       filterBox.setPadding(new Insets(20, 20, 0, 40)); // Haut, Droite, Bas, Gauche

       Label filtreTaille = new Label("Tailles");
       sizeXS = new CheckBox("Taille XS");
       sizeS = new CheckBox("Taille S");
       sizeM = new CheckBox("Taille M");
       sizeL = new CheckBox("Taille L");
       size36 = new CheckBox("36");
       size37 = new CheckBox("37");
       size38 = new CheckBox("38");
       size39 = new CheckBox("39");              
       filterBox.getChildren().addAll(filtreTaille, sizeXS, sizeS, sizeM, sizeL, size36, size37, size38, size39);
       
       Label genderFilterLabel = new Label("Genre");
       male = new CheckBox("Homme");
       female = new CheckBox("Femme");
       child = new CheckBox("Enfant");
       filterBox.getChildren().addAll(genderFilterLabel, male, female, child);
       
       Label filtreMarque = new Label("Marques");
       marqueAsics = new CheckBox("Asics");
       marqueAdidas = new CheckBox("Adidas");
       marqueBabolat = new CheckBox("Babolat");
       filterBox.getChildren().addAll(filtreMarque, marqueAsics, marqueAdidas, marqueBabolat);
       
       Label clothingTypeFilterLabel = new Label("Types");
       tankTop = new CheckBox("Debardeur");
       sweat = new CheckBox("Sweat");
       shorts = new CheckBox("Short");
       tshirt = new CheckBox("Tshirt");
       dress = new CheckBox("Robe");
       filterBox.getChildren().addAll(clothingTypeFilterLabel, tankTop, sweat, shorts, tshirt, dress);
       
       Label filtrePrix = new Label("Prix maximum");
       Slider priceSlider = new Slider();
       priceSlider.setMin(0); // Borne minimum
       priceSlider.setMax(200); // Borne maximum
       priceSlider.setValue(200); // Valeur initiale
       priceSlider.setShowTickMarks(true); // Affiche les graduations
       priceSlider.setShowTickLabels(true); // Affiche les étiquettes
       priceSlider.setMajorTickUnit(50); // Intervalles des graduations principales
       priceSlider.setBlockIncrement(20); // Incréments avec le clavier ou la souris
       Label priceLabel = new Label("Prix maximum sélectionné : " + (int) priceSlider.getValue() + " €");
       priceLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: normal");
       filterBox.getChildren().addAll(filtrePrix, priceSlider, priceLabel);
       
       sizeXS.setOnAction(event -> applyFilters(mainView));
       sizeS.setOnAction(event -> applyFilters(mainView));
       sizeM.setOnAction(event -> applyFilters(mainView));
       sizeL.setOnAction(event -> applyFilters(mainView));
       size36.setOnAction(event -> applyFilters(mainView));
       size37.setOnAction(event -> applyFilters(mainView));
       size38.setOnAction(event -> applyFilters(mainView));
       size39.setOnAction(event -> applyFilters(mainView));       
       male.setOnAction(event -> applyFilters(mainView));
       female.setOnAction(event -> applyFilters(mainView));
       child.setOnAction(event -> applyFilters(mainView));
       marqueAsics.setOnAction(event -> applyFilters(mainView));
       marqueAdidas.setOnAction(event -> applyFilters(mainView));
       marqueBabolat.setOnAction(event -> applyFilters(mainView));
       tankTop.setOnAction(event -> applyFilters(mainView));
       sweat.setOnAction(event -> applyFilters(mainView));
       shorts.setOnAction(event -> applyFilters(mainView));
       tshirt.setOnAction(event -> applyFilters(mainView));
       dress.setOnAction(event -> applyFilters(mainView));
       priceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
    	   priceLabel.setText("Prix maximum sélectionné : " + newValue.intValue() + " €");
           maxPrice = newValue.doubleValue(); // Met à jour la variable globale maxPrice
           applyFilters(mainView); // Applique les filtres
       });
       
       return filterBox;
   }
   
   private void applyFilters(MainView mainView) {
	   	// Collecte des filtres sélectionnés
	    sizeSelected = new HashSet<>();
	    if (sizeXS.isSelected()) sizeSelected.add("XS");
	    if (sizeS.isSelected()) sizeSelected.add("S");
	    if (sizeM.isSelected()) sizeSelected.add("M");
	    if (sizeL.isSelected()) sizeSelected.add("L");
	    if (size36.isSelected()) sizeSelected.add("36");
	    if (size37.isSelected()) sizeSelected.add("37");
	    if (size38.isSelected()) sizeSelected.add("38");
	    if (size39.isSelected()) sizeSelected.add("39");

	    genderSelected = new HashSet<>();
	    if (male.isSelected()) genderSelected.add("Homme");
	    if (female.isSelected()) genderSelected.add("Femme");
	    if (child.isSelected()) genderSelected.add("Enfant");
	    
	    brandSelected = new HashSet<>();
	    if (marqueAsics.isSelected()) brandSelected.add("Asics");
	    if (marqueAdidas.isSelected()) brandSelected.add("Adidas");
	    if (marqueBabolat.isSelected()) brandSelected.add("Babolat");
	    
	    clothingTypeSelected = new HashSet<>();
	    if (tankTop.isSelected()) clothingTypeSelected.add("Debardeur");
	    if (sweat.isSelected()) clothingTypeSelected.add("Sweat");
	    if (shorts.isSelected()) clothingTypeSelected.add("Short");
	    if (tshirt.isSelected()) clothingTypeSelected.add("Tshirt");
	    if (dress.isSelected()) clothingTypeSelected.add("Robe");
	    updateProductDisplay(mainView);
	}

   private boolean filterByAttributes(Product product) {
	    boolean matchSize = sizeSelected.isEmpty() || ((ProductWithSize) product).getTailleStock().keySet().stream().anyMatch(sizeSelected::contains);
	    boolean matchGender = genderSelected.isEmpty() || genderSelected.contains(((ProductWithSize) product).getGenre());
	    boolean matchBrand = brandSelected.isEmpty() || brandSelected.contains(product.getMarque());
	    boolean matchClothingType = true;
		if (product instanceof Vetement) {
			matchClothingType = clothingTypeSelected.isEmpty() || clothingTypeSelected.stream()
		            .anyMatch(type -> type.equalsIgnoreCase(((Vetement) product).getTypeVetement().toString()));
		}
		boolean isClothingSelected = !clothingTypeSelected.isEmpty();
	    if (isClothingSelected && product instanceof Chaussures) {
	        return false;  // Exclure les produits de type Chaussures
	    }
	    return matchSize && matchGender && matchBrand && matchClothingType;
   }
   
   private boolean filterByPrice(Product product) {
	    return product.getPrice() <= maxPrice;
	}
   
   private void updateProductDisplay(MainView mainView) {
	    produitsGrid.getChildren().clear(); // Nettoie l'affichage actuel
	    actualProducts.stream()
	       .filter(this::filterByAttributes)
	       .filter(this::filterByPrice)
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
