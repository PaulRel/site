package Interface;

import java.io.IOException;
import java.util.List;

import customer.Customer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import products.Product;

public class MainView extends Application {

	private Stage primaryStage;
	private HeaderView headerView;
	private String css;
	private static Customer currentCustomer;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	this.primaryStage=primaryStage;
       	headerView = new HeaderView(this);
        
        showProductView(Product.class, null);
        
        setupStage(primaryStage);
        primaryStage.show();
    }
    
    /**
     * Configure la scène principale de l'application et applique les styles CSS.
     * 
     * @param primaryStage La scène principale de l'application.
     * @throws IOException En cas d'erreur de chargement des ressources.
     */
    public void setupStage(Stage primaryStage) throws IOException{      
        Image icon = new Image (getClass().getResource("/Image/logo.jpg").toExternalForm());
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Magasin de tennis");
        primaryStage.setFullScreen(false); //A remettre vrai
    }
    
    /**
     * Crée et place la section de produits au centre de l'interface utilisateur.
     */
    public void showProductView(Class<? extends Product> typeProduct, List<Product> products) {
    	ProductView productSection = new ProductView(this, typeProduct, products);
    	AnchorPane root = productSection.getRoot();
    	root.getChildren().add(headerView.getHeader()); // Ajouter l'en-tête
    	Scene productScene = new Scene(root, 1350, 670);
    	productScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
    	primaryStage.setScene(productScene);          
    }
    
    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }
    
	public Stage getPrimaryStage() {
		return primaryStage;
	}
    
    /**
     * Crée un conteneur défilant (ScrollPane) pour la mise en page principale, permettant le défilement du contenu.
     */
    public static ScrollPane createScrollPane(Pane root) {
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true); // Le contenu s'adapte à la largeur de la fenêtre
        scrollPane.setPannable(true);   // Permet le défilement via la souris
        //scrollPane.getStyleClass().add("scroll-pane-style"); // Appliquer une classe CSS (facultatif)
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Barre de défilement verticale au besoin
        scrollPane.setPadding(new Insets(10));
        scrollPane.setStyle("-fx-background-color: derive(#ececec,26.4%)");
        AnchorPane.setTopAnchor(scrollPane, 118.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        scrollPane.setPrefViewportHeight(520);
        return scrollPane;
    }
	
	// Méthode pour afficher une boîte d'alerte
    public static void showAlert(String title, String headerText, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }
}