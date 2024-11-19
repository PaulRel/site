package Interface;

import customer.Cart;
import customer.CartItem;
import customer.CartManager;
import customer.Customer;
import customer.Order;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import products.Produit;

public class CartView {
	private TableView<CartItem> cartTable = new TableView<CartItem>();
    Cart cart = CartManager.getTempCart();
    private VBox root;
    private HBox buttonsBox;
    private Button continueButton, orderButton;
	
	public CartView(MainView mainView, Stage primaryStage) {
		continueButton = new Button("Continuer vos achats");
		continueButton.setOnAction(e -> mainView.showProductView(Produit.class));
		if (cart == null || cart.getItems().isEmpty()) {
			Label emptyCartLabel = new Label("Votre panier est vide");
				
		// Mise en page principale
        //VBox main = new VBox(emptyCartLabel, continueButton);
        //main.setPadding(new Insets(10));
        //main.setMaxSize(700, 400);
        //main.setAlignment(Pos.CENTER);
        //main.setStyle("-fx-background-color: white");
		
			root = new VBox(20, emptyCartLabel, continueButton);
			AnchorPane.setTopAnchor(root, 116.0);
		}
		else {
			displayCart(mainView, primaryStage);
		}
	    root.setPadding(new Insets(30));
	    root.setPrefSize(1350, 550);
	    root.setStyle("-fx-background-color: #EEEEEE");
	    root.setAlignment(Pos.TOP_CENTER);
	    primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {root.setPrefWidth(newValue.doubleValue());}); // Ajuste la largeur
	    primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {root.setPrefHeight(newValue.doubleValue()-116);}); // Ajuste la hauteur
	    
	    AnchorPane rootPane = new AnchorPane();
	    HeaderView v = new HeaderView(mainView, primaryStage);      
	    rootPane.getChildren().addAll(v.getHeader(), root);
	    
	    Scene createAccountScene = new Scene(rootPane, 1350, 670);
	               
	    String css = this.getClass().getResource("/style.css").toExternalForm();        
	    createAccountScene.getStylesheets().add(css);
	        
	    primaryStage.setScene(createAccountScene);
	}
	
	public void displayCart(MainView mainView, Stage primaryStage) {
		if (cartTable.getColumns().isEmpty()) {
			// Ajouter une colonne pour l'image
	        TableColumn<CartItem, ImageView> imageColumn = new TableColumn<>("Image");
	        imageColumn.setCellValueFactory(cellData -> {
	            // Charger l'image à partir du chemin de l'image
	            String imagePath = cellData.getValue().getProduct().getImagePath();
	            ImageView imageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));

	            // Configurer les dimensions de l'image (facultatif)
	            imageView.setFitWidth(50); // Largeur de l'image
	            imageView.setFitHeight(50); // Hauteur de l'image
	            imageView.setPreserveRatio(true);

	            return new SimpleObjectProperty<>(imageView);
	        });
	        
	        TableColumn<CartItem, String> productColumn = new TableColumn<>("Produit");
	        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getNom()));

	        TableColumn<CartItem, String> sizeColumn = new TableColumn<>("Taille");
	        sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSize()));

	        TableColumn<CartItem, Integer> quantityColumn = new TableColumn<>("Quantité");
	        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
	        
	     // Créer une colonne pour le bouton d'action
	        TableColumn<CartItem, Void> actionColumn = new TableColumn<>("Action");
	        actionColumn.setCellFactory(param -> new TableCell<>() {
	            private final Button deleteButton = new Button("Supprimer"); {
	                deleteButton.setOnAction(event -> {	                    
	                    CartItem cartItem = getTableView().getItems().get(getIndex()); // Récupérer l'article correspondant à cette ligne                    
	                    cart.getItems().remove(cartItem); // Supprimer l'article du panier
	                    getTableView().getItems().remove(cartItem); // Mettre à jour la TableView
	                });
	            }

	            @Override
	            protected void updateItem(Void item, boolean empty) {
	                super.updateItem(item, empty);
	                if (empty) {
	                    setGraphic(null); // Pas de bouton pour les lignes vides
	                } else {
	                    setGraphic(deleteButton); // Afficher le bouton pour les lignes valides
	                }
	            }
	        });	        
	        cartTable.getColumns().add(imageColumn);
	        cartTable.getColumns().add(productColumn);
	        cartTable.getColumns().add(sizeColumn);
	        cartTable.getColumns().add(quantityColumn);
	        cartTable.getColumns().add(actionColumn);
	    }
		ObservableList<CartItem> observableItems = FXCollections.observableArrayList(cart.getItems());
	    cartTable.setItems(observableItems);
	    orderButton = new Button("Commander");
	    orderButton.setOnAction(e -> validateOrder(mainView, primaryStage));
	    buttonsBox = new HBox(100, continueButton, orderButton);
	    root = new VBox(30, cartTable, buttonsBox);
		AnchorPane.setTopAnchor(root, 116.0);
	}
	
	private void validateOrder(MainView mainView, Stage primaryStage) {
		if (MainView.getCurrentCustomer()==null) {
			new AuthController(mainView, primaryStage);
		}
	    Customer currentCustomer = MainView.getCurrentCustomer();
	    AuthController.syncUserCart();
		Order order = new Order(currentCustomer, currentCustomer.getCart().getItems());

	    for (CartItem item : cart.getItems()) {
	        order.decrementStock(item.getProduct().getId(), item.getSize(), item.getQuantity());
	    }

	    cart.clearCart();
	    //showMessage("Order validated!");
	}

}
