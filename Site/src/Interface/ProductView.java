package Interface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import database.ProductDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import products.Chaussures;
import products.Product;
import products.Vetement;
import products.ProductWithSize;

public class ProductView {
	
	private AnchorPane root;
	private FlowPane produitsGrid;
	private ProductDAO productDAO;
	List<Product> actualProducts;
	private ComboBox<String> sortComboBox;
	private Set<String> sizeSelected, genderSelected, brandSelected, clothingTypeSelected;
	private CheckBox sizeXS, sizeS, sizeM, sizeL, size36, size37, size38, size39, size40, size41, male, female, asicsBrand, babolatBrand, adidasBrand, tankTop, sweat, shorts, tshirt, dress;
	private double maxPrice = 200.0;
	
	public ProductView(MainView mainView, Class<? extends Product> productType, List<Product> products) {
		root = new AnchorPane();
        root.getChildren().addAll(MainView.createScrollPane(createFilterBox(mainView)), displayProducts(mainView, productType, products));
	}
	
	/**
    * Affiche une grille de produits dans une interface utilisateur de type GridPane.
    * 
    * @param primaryStage La scène principale de l'application.
    * @return Le GridPane contenant tous les produits.
    */
   public ScrollPane displayProducts(MainView mainView, Class<? extends Product> typeProduit, List<Product> products) {
	   if (produitsGrid == null) {
		   produitsGrid = new FlowPane();
		   produitsGrid.setPadding(new Insets(20));
		   produitsGrid.setHgap(10); //espace horizontal entre les produits
		   produitsGrid.setVgap(10);
		   produitsGrid.setPrefSize(900, 800);
	   }
       
       if (products == null) {
    	   productDAO = new ProductDAO();  // Récupérer les produits depuis la base de données
    	   products = productDAO.getAllProduits();
       }
       
       actualProducts = new ArrayList<Product>();
       if (produitsGrid.getChildren().isEmpty()) {
           actualProducts = products.stream()
               .filter(typeProduit::isInstance)
               .toList(); // Utilisation de toList pour créer une copie immuable

           // Chargement différé des éléments dans un thread séparé
           Task<Void> loadProductsTask = new Task<>() {
               @Override
               protected Void call() {
                   for (Product produit : actualProducts) {
                       VBox produitBox = createProductBox(mainView, produit);
                       
                       // Mise à jour de l'interface graphique sur le thread principal
                       Platform.runLater(() -> produitsGrid.getChildren().add(produitBox));
                   }
                   return null;
               }
           };

           new Thread(loadProductsTask).start();
       }

  
       // Encapsuler le FlowPane dans un ScrollPane
       ScrollPane scrollPane = MainView.createScrollPane(produitsGrid);
       AnchorPane.setLeftAnchor(scrollPane, 250.0);
       AnchorPane.setRightAnchor(scrollPane, 0.0); // S'assurer qu'il occupe tout l'espace horizontal
       //AnchorPane.setTopAnchor(scrollPane, 116.0);
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
       ImageView imageView = new ImageView();
       imageView.setFitHeight(150);
       imageView.setFitWidth(150);
       
       Task<Image> loadImageTask = new Task<>() {
           @Override
           protected Image call() {
               return new Image(getClass().getResource(product.getImagePath()).toExternalForm());
           }
       };

       loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));
       new Thread(loadImageTask).start();
       

       Label nomProduit = new Label(product.getName());
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
       filterBox.setPrefWidth(230);
       filterBox.setPadding(new Insets(20, 20, 0, 40)); // Haut, Droite, Bas, Gauche
       filterBox.setSpacing(3);
       //AnchorPane.setTopAnchor(filterBox, 116.0);
       
       Label sortByPrice = new Label("Tri");
       sortComboBox = new ComboBox<>(FXCollections.observableArrayList("", "Prix croissant", "Prix décroissant", "Nom (de A à Z)", "Nom (de Z à A)"));
       filterBox.getChildren().addAll(sortByPrice, sortComboBox);

       Label filters = new Label("Filtres");
       
       Label genderFilterLabel = new Label("Genre");
       male = new CheckBox("Homme");
       female = new CheckBox("Femme");
       filterBox.getChildren().addAll(genderFilterLabel, male, female);
       
       Label brandFilterLabel = new Label("Marques");
       asicsBrand = new CheckBox("Asics");
       adidasBrand = new CheckBox("Adidas");
       babolatBrand = new CheckBox("Babolat");
       filterBox.getChildren().addAll(brandFilterLabel, asicsBrand, adidasBrand, babolatBrand);
       
       Label sizeFilterLabel = new Label("Tailles");
       sizeXS = new CheckBox("Taille XS");
       sizeS = new CheckBox("Taille S");
       sizeM = new CheckBox("Taille M");
       sizeL = new CheckBox("Taille L");
       size36 = new CheckBox("36");
       size37 = new CheckBox("37");
       size38 = new CheckBox("38");
       size39 = new CheckBox("39");
       size40 = new CheckBox("40");
       size41 = new CheckBox("41");
       filterBox.getChildren().addAll(filters, sizeFilterLabel, sizeXS, sizeS, sizeM, sizeL, size36, size37, size38, size39, size40, size41);
       
       Label clothingTypeFilterLabel = new Label("Types");
       tankTop = new CheckBox("Debardeur");
       sweat = new CheckBox("Sweat");
       shorts = new CheckBox("Short");
       tshirt = new CheckBox("Tshirt");
       dress = new CheckBox("Robe");
       filterBox.getChildren().addAll(clothingTypeFilterLabel, tankTop, sweat, shorts, tshirt, dress);
       
       Label priceFilterLabel = new Label("Prix maximum");
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
       filterBox.getChildren().addAll(priceFilterLabel, priceSlider, priceLabel);
       
       for (Label labels : new Label[]{sizeFilterLabel, genderFilterLabel, brandFilterLabel, clothingTypeFilterLabel, priceFilterLabel}) {
    	   labels.setStyle("-fx-font-weight: normal ");
          }
       
       sortComboBox.setOnAction(e -> updateProductDisplay(mainView));
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
       asicsBrand.setOnAction(event -> applyFilters(mainView));
       adidasBrand.setOnAction(event -> applyFilters(mainView));
       babolatBrand.setOnAction(event -> applyFilters(mainView));
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
       
       sizeSelected = new HashSet<>();
       genderSelected = new HashSet<>();	    
	   brandSelected = new HashSet<>();
	   clothingTypeSelected = new HashSet<>();
       
       return filterBox;
   }
   
   private void applyFilters(MainView mainView) {
	   // Collecte des filtres sélectionnés	
	    sizeSelected.clear();  	
	    if (sizeXS.isSelected()) sizeSelected.add("XS");
	    if (sizeS.isSelected()) sizeSelected.add("S");
	    if (sizeM.isSelected()) sizeSelected.add("M");
	    if (sizeL.isSelected()) sizeSelected.add("L");
	    if (size36.isSelected()) sizeSelected.add("36");
	    if (size37.isSelected()) sizeSelected.add("37");
	    if (size38.isSelected()) sizeSelected.add("38");
	    if (size39.isSelected()) sizeSelected.add("39");
	    
	    genderSelected.clear();
	    if (male.isSelected()) genderSelected.add("Homme");
	    if (female.isSelected()) genderSelected.add("Femme");
	    
	    brandSelected.clear();
	    if (asicsBrand.isSelected()) brandSelected.add("Asics");
	    if (adidasBrand.isSelected()) brandSelected.add("Adidas");
	    if (babolatBrand.isSelected()) brandSelected.add("Babolat");
	    
	    clothingTypeSelected.clear();
	    if (tankTop.isSelected()) clothingTypeSelected.add("Debardeur");
	    if (sweat.isSelected()) clothingTypeSelected.add("Sweat");
	    if (shorts.isSelected()) clothingTypeSelected.add("Short");
	    if (tshirt.isSelected()) clothingTypeSelected.add("Tshirt");
	    if (dress.isSelected()) clothingTypeSelected.add("Robe");
	    updateProductDisplay(mainView);
	}
   
   private Comparator<Product> sort() {
	    if ("Prix croissant".equals(sortComboBox.getValue())) {
	        return Comparator.comparingDouble(Product::getPrice); // Tri par prix croissant
	    } else if ("Prix décroissant".equals(sortComboBox.getValue())) {
	        return Comparator.comparingDouble(Product::getPrice).reversed(); // Tri par prix décroissant
   		} else if ("Nom (de A à Z)".equals(sortComboBox.getValue())) {
   			return Comparator.comparing(Product::getName); // Tri par nom ordre alphabétique
   		} else if ("Nom (de Z à A)".equals(sortComboBox.getValue())) {
   			return Comparator.comparing(Product::getName).reversed(); // Tri par nom ordre inverse
   		}
	    return (p1, p2) -> 0; // Par défaut, aucun tri spécifique
	}

   private boolean filterByAttributes(Product product) {
	    boolean matchSize = sizeSelected.isEmpty() || ((ProductWithSize) product).getTailleStock().keySet().stream().anyMatch(sizeSelected::contains);
	    boolean matchGender = genderSelected.isEmpty() || genderSelected.contains(((ProductWithSize) product).getGender());
	    boolean matchBrand = brandSelected.isEmpty() || brandSelected.contains(product.getBrand());
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
	    .sorted(sort())
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
