package Interface;

import java.util.HashMap;
import java.util.Map;

import customer.Cart;
import customer.CartManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import products.ProductWithSize;
import products.Product;

public class ProductDetailsView {
	private int chosenQuantity;
	Cart cart = CartManager.getTempCart();
	
	public ProductDetailsView(MainView mainView, Product product) {
		showProductDetails(mainView, product);
	}
	/**
     * Affiche les détails d'un produit spécifique dans une nouvelle scène.
     * 
     * @param primaryStage La scène principale de l'application.
     * @param produit      Le produit dont les détails sont à afficher.
     */
    public void showProductDetails(MainView mainView, Product product) {
        // Création d'un nouveau conteneur pour les détails du produit
        HBox detailsBox = new HBox();
        detailsBox.setPadding(new Insets(20));
        detailsBox.setSpacing(10);
        detailsBox.setMaxSize(1200, 500);
        AnchorPane.setTopAnchor(detailsBox, 116.0);
        
        // Affichage des informations du produit
        Label nameLabel = new Label(product.getName());
        Label priceLabel = new Label("Prix : " + product.getPrice() + "€");
        Label descriptionLabel = new Label(product.getDescription() + "\nChoisir la taille :");
        Label selectedQuantityLabel = new Label("Quantité souhaitée :  ");
        descriptionLabel.setStyle("-fx-font-weight: normal");
        descriptionLabel.setWrapText(true);
        
        Button addToCartButton = new Button("Ajouter au panier");
        addToCartButton.setDisable(true); // Le bouton est désactivé par défaut
        
     // ComboBox for the desired quantity
        ComboBox<Integer> quantityComboBox = new ComboBox<>();
        quantityComboBox.setDisable(true); // Disabled by default
        quantityComboBox.getItems().add(1); // Default value
        quantityComboBox.setValue(1); // Set default quantity
        
        ComboBox<String> sizeChoiceBox = new ComboBox<>();
        
        // ComboBox pour afficher les tailles et quantités disponibles
        if (product instanceof ProductWithSize) {
        	ProductWithSize productWithSize = (ProductWithSize) product;
        	HashMap<String, Integer> sizesStock = productWithSize.getTailleStock();
        	//System.out.println("Map " + sizesStock);
        	
        	 // Ajouter les éléments formatés dans la ComboBox
        	sizeChoiceBox.getItems().clear();
            for (Map.Entry<String, Integer> entry : sizesStock.entrySet()) {
                String size = entry.getKey();
                int qtDispo = entry.getValue();
                String text;
                if (qtDispo == 0) {
                	text = "En rupture de stock";
                } 
                else if (qtDispo <= 5) {
                    text = "Plus que " + qtDispo + " en stock";
                } else {
                    text = "Stock 5+";
                }	
                sizeChoiceBox.getItems().add(size + " : " + text);
            }

            // Quand l'utilisateur choisit une taille, on active la combobox permettant de choisir la qte souhaite de cette taille
            sizeChoiceBox.setOnAction(event -> {
            	addToCartButton.setDisable(sizeChoiceBox.getValue() == null);
            	String selectedSize = sizeChoiceBox.getValue(); // e.g., "39 : Plus que 1 en stock"

                // Extract the size and find the available quantity
                if (selectedSize != null) {
                    String chosenSize = selectedSize.split(" :")[0];
                    int availableQuantity = sizesStock.get(chosenSize);

                    // Update the options in the quantity ComboBox
                    ObservableList<Integer> quantities = FXCollections.observableArrayList();
                    
                    // Le client ne peut pas acheter plus de 5 fois le meme produit sur vue -> evite liste trop longue
                    for (int i = 1; i <= Math.min(availableQuantity, 5); i++) {
                        quantities.add(i);
                    }

                    quantityComboBox.setItems(quantities);
                    quantityComboBox.setValue(1); // Reset default value
                    quantityComboBox.setDisable(false); // Enable the ComboBox
                }
            });
        }
        
        ImageView imageView = new ImageView(new Image(getClass().getResource(product.getImagePath()).toExternalForm()));
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        
        Label addProductLabel = new Label();
        
        addToCartButton.setOnAction(e -> {
        	 String selectedSize = sizeChoiceBox.getValue().split(" :")[0];
        	 chosenQuantity = quantityComboBox.getValue();
        	 
        	 if (MainView.getCurrentCustomer() != null) {cart = MainView.getCurrentCustomer().getCart();}
        	 cart.addProduct(product, selectedSize, chosenQuantity);
        	 
        	 addProductLabel.setText("Produit ajouté au panier!");
        });
        
        VBox descriptionBox = new VBox (nameLabel, priceLabel, descriptionLabel, sizeChoiceBox, selectedQuantityLabel, quantityComboBox, addToCartButton, addProductLabel);
        
        // Ajouter les éléments à la boîte de détails
        detailsBox.getChildren().addAll(imageView, descriptionBox);
        
        HeaderView v=new HeaderView(mainView);
        AnchorPane rootPane = new AnchorPane();
        rootPane.getChildren().addAll(v.getHeader(), detailsBox);
        
        // Nouvelle scène pour les détails
        Scene detailScene = new Scene(rootPane, 1350, 670);
        detailScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        mainView.getPrimaryStage().setScene(detailScene);
    }
}
