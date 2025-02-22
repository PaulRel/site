package Interface;

import java.util.Optional;

import customer.Cart;
import customer.CartItem;
import customer.CartManager;
import customer.Order;
import database.OrderDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import products.Product;

/**
 * Classe représentant l'interface graphique du panier.
 * Elle permet d'afficher les articles ajoutés au panier, de les modifier ou de les supprimer,
 * et de passer une commande.
 */
public class CartView {
	private TableView<CartItem> cartTable = new TableView<CartItem>();
    Cart cart = CartManager.getTempCart();
    private VBox root;
    private HBox buttonsBox;
    private Button continueButton, orderButton;
	
    /**
     * Constructeur de la vue du panier.
     *
     * @param mainView La vue principale de l'application.
     */
	public CartView(MainView mainView) {
		
		// Lorsque l'utilisateur clique sur le bouton Continuer vos achats, la vue des produits est affichée
		continueButton = new Button("Continuer vos achats");
		continueButton.setOnAction(e -> mainView.showProductView(Product.class, null));
		
		// Récupération du panier de l'utilisateur actuel, si un utilisateur est connecté
		if (MainView.getCurrentCustomer() != null) {cart = MainView.getCurrentCustomer().getCart();}
		
		// Si le panier est vide ou inexistant, affichage Votre panier est vide
		if (cart == null || cart.getItems().isEmpty()) {
			Label emptyCartLabel = new Label("Votre panier est vide");
			root = new VBox(20, emptyCartLabel, continueButton);
			AnchorPane.setTopAnchor(root, 118.0);
		}  
		// Sinon, affiche les éléments du panier en utilisant la méthode displayCart
		else {
			displayCart(mainView);
		}
		
		
	    root.setPadding(new Insets(30));
	    root.setPrefSize(1368, 690);
	    root.setStyle("-fx-background-color: #EEEEEE");
	    root.setAlignment(Pos.TOP_CENTER);

	    AnchorPane rootPane = new AnchorPane();
	    HeaderView v = new HeaderView(mainView);
	    rootPane.getChildren().addAll(v.getHeader(), root);
	    
	    Scene createAccountScene = new Scene(rootPane, 1368, 690);
	    createAccountScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
	        
	    mainView.getPrimaryStage().setScene(createAccountScene);
	}
	
	/**
     * Affiche le panier sous forme de table avec les détails des articles et l'option de suppression.
     * Ajoute également les boutons "Continuer" et "Commander".
     * Met en page les composants dans la VBox root
     *
     * @param mainView La vue principale de l'application.
     */
	public void displayCart(MainView mainView) {
		
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
	        productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));

	        TableColumn<CartItem, String> sizeColumn = new TableColumn<>("Taille");
	        sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSize()));

	        TableColumn<CartItem, Integer> quantityColumn = new TableColumn<>("Quantité");
	        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
	        
	        TableColumn<CartItem, Double> priceColumn = new TableColumn<>("Prix à l'unite");
	        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getProduct().getPrice()).asObject());
	        
	        // Créer une colonne pour le bouton d'action
	        TableColumn<CartItem, Void> actionColumn = new TableColumn<>("Action");
	        actionColumn.setCellFactory(param -> new TableCell<>() {
	            private final Button deleteButton = new Button("Supprimer"); {
	                deleteButton.setOnAction(event -> {	                    
	                    CartItem cartItem = getTableView().getItems().get(getIndex()); // Récupérer l'article correspondant à cette ligne                    
	                    cart.getItems().remove(cartItem); // Supprimer l'article du panier
	                    getTableView().getItems().remove(cartItem); // Mettre à jour la TableView
	                    if (cart.getItems().isEmpty()) {
	            			new CartView(mainView);
	                    }
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
	        
	        // Ajout des colonnes à la table
	        cartTable.getColumns().add(imageColumn);
	        cartTable.getColumns().add(productColumn);
	        cartTable.getColumns().add(sizeColumn);
	        cartTable.getColumns().add(priceColumn);
	        cartTable.getColumns().add(quantityColumn);
	        cartTable.getColumns().add(actionColumn);
	        
	        // Définition des largeurs minimales des colonnes
	        imageColumn.setMinWidth(100);
	        productColumn.setMinWidth(450);
	        sizeColumn.setMinWidth(100);
	        priceColumn.setMinWidth(100);
	        quantityColumn.setMinWidth(100);
	        actionColumn.setMinWidth(100);
	        
	    }
	
		// Ajout des articles à la table
		ObservableList<CartItem> observableItems = FXCollections.observableArrayList(cart.getItems());
	    cartTable.setItems(observableItems);
	    cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    // Bouton de commande
	    orderButton = new Button("Commander");
	    orderButton.setOnAction(e -> validateOrder(mainView));
	    
	    // Ajout des boutons sous la table
	    buttonsBox = new HBox(100, continueButton, orderButton);
	    root = new VBox(30, cartTable, buttonsBox);
		AnchorPane.setTopAnchor(root, 118.0);
	}
	
	/**
	 * Valide la commande en cours pour l'utilisateur connecté.
	 * Si aucun utilisateur n'est connecté, affiche un avertissement pour se connecter.
	 * Sinon 
	 * - Synchronise le panier de l'utilisateur avec la base de données.
	 * - Crée une nouvelle commande et y associe les articles du panier.
	 * - Insère la commande dans la base de données, décrémente les stocks des produits commandés et vide le panier.
	 * 
	 * @param mainView La vue principale de l'application.
	 */
	private void validateOrder(MainView mainView) {
		if (MainView.getCurrentCustomer()==null) {
			displayLoginWarning(mainView);
		}
		else {
			AuthentificationView.syncUserCart();
			Order order = new Order(MainView.getCurrentCustomer());
			order.setProducts(cart.getItems());
			
			// Inserer la commande dans la base de données
			OrderDAO orderDAO = new OrderDAO();
            orderDAO.insertOrder(order);

            // Décrémenter le stock pour chaque produit commandé
			for (CartItem item : cart.getItems()) {
				order.decrementStock(item.getProduct().getId(), item.getSize(), item.getQuantity());
			}
			
			// Vider le panier et afficher la page de commande
			cart.clearCart();
			new OrderView(mainView, order);
		}
	}
	
	/**
     * Affiche une alerte demandant à l'utilisateur de se connecter avant de passer commande.
     * Propose un bouton permettant d'accéder à la page de connexion.
     *
     * @param mainView La vue principale de l'application.
     */
	public void displayLoginWarning(MainView mainView) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Authentification requise");
		alert.setHeaderText("Vous n'êtes pas connecté");
		alert.setContentText("Veuillez vous authentifier pour passer votre commande.");

		// Ajouter un bouton pour rediriger vers la page de connexion
		ButtonType loginButton = new ButtonType("Se connecter");
		ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(loginButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == loginButton) {
			new AuthentificationView(mainView);
		}

	}
}
