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

/**
 * La classe MainView représente la vue principale de l'application. Elle est responsable
 * de l'affichage de l'interface utilisateur, de la gestion de la scène principale et de 
 * l'interaction avec l'utilisateur.
 */
public class MainView extends Application {

	private Stage primaryStage;
	private HeaderView headerView;
	private static Customer currentCustomer;

	/**
     * Point d'entrée principal de l'application. Lance l'application JavaFX.
     * 
     * @param args Les arguments de ligne de commande.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Méthode de démarrage de l'application. Configure la scène principale et 
     * affiche la vue des produits.
     * 
     * @param primaryStage La fenêtre principale de l'application.
     * @throws Exception Si une erreur se produit lors de l'initialisation.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
    	this.primaryStage=primaryStage;
       	headerView = new HeaderView(this);   
        showProductView(Product.class, null);
        setupStage();
        primaryStage.show();
    }
    
    /**
     * Configure la scène principale de l'application, définit l'icône de l'application 
     * et le titre de la fenêtre principale.
     * 
     * @throws IOException En cas d'erreur lors du chargement des ressources (icône, etc.).
     */
    public void setupStage() throws IOException{      
        Image icon = new Image (getClass().getResource("/Image/logo.jpg").toExternalForm());
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Magasin de tennis");
        primaryStage.setFullScreen(false); //A remettre vrai
    }
    
    /**
     * Crée et place la section des produits au centre de l'interface utilisateur.
     * 
     * @param typeProduct La classe des produits à afficher.
     * @param products La liste des produits à afficher (peut être null si on veut afficher tous les produits).
     */
    public void showProductView(Class<? extends Product> typeProduct, List<Product> products) {
    	ProductView productSection = new ProductView(this, typeProduct, products);
    	AnchorPane root = productSection.getRoot();
    	root.getChildren().add(headerView.getHeader()); // Ajouter l'en-tête
    	Scene productScene = new Scene(root, 1368, 690);
    	productScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
    	primaryStage.setScene(productScene);          
    }
    
    /**
     * Retourne le client actuellement connecté.
     * 
     * @return Le client actuel.
     */
    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * Définit le client actuellement connecté.
     * 
     * @param customer Le client à définir comme client actuel.
     */
    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }
    
    /**
     * Retourne la fenêtre principale (primaryStage) de l'application.
     * 
     * @return La fenêtre principale.
     */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
    
	/**
     * Crée un conteneur défilant (ScrollPane) pour la mise en page principale, permettant
     * le défilement du contenu.
     * 
     * @param root Le conteneur racine à afficher dans le ScrollPane.
     * @return Le ScrollPane configuré avec le conteneur racine.
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
	
    /**
     * Affiche une boîte de dialogue d'alerte.
     * 
     * @param title Le titre de la boîte de dialogue.
     * @param headerText Le texte d'en-tête de la boîte de dialogue.
     * @param message Le message à afficher dans la boîte de dialogue.
     * @param type Le type d'alerte à afficher (par exemple, erreur, information, etc.).
     */
    public static void showAlert(String title, String headerText, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }
}