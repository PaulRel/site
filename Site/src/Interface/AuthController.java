package Interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import customer.Cart;
import customer.CartItem;
import customer.CartManager;
import customer.Customer;
import customer.Order;
import database.DatabaseConnection;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AuthController {
    private AnchorPane rootPane;
    private Label mainLabel;

    public AuthController(MainView mainView, Stage primaryStage) {
    	// Création des labels principaux
        mainLabel = new Label("Identifiez-vous ou créez un compte");
        
    	rootPane = new AnchorPane();
        createBox(mainView, primaryStage);
        
        Scene authScene = new Scene(rootPane, 1350, 670);
        
        HeaderView v=new HeaderView(mainView, primaryStage);      
        rootPane.getChildren().addAll(v.getHeader());
        String css = this.getClass().getResource("/style.css").toExternalForm();        
        authScene.getStylesheets().add(css);
        primaryStage.setScene(authScene);
    }

    private void createBox(MainView mainView, Stage primaryStage) {        
        // Section pour les clients existants (VBox gauche)
        VBox existingCustomerBox = new VBox(10);
        existingCustomerBox.setPrefSize(600, 400);
        
        Label existingLabel = new Label("Déjà clients chez TennisShop");
        
        Label instructionLabel = new Label("Si vous avez déjà un compte, veuillez vous identifier.");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal");
        
        // Champs de connexion
        TextField emailField = new TextField();
        emailField.setPromptText("Adresse mail *");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe *");
        
        Hyperlink forgotPasswordLink = new Hyperlink("Mot de passe oublié ?");
        
        Button loginButton = new Button("CONNEXION");
        loginButton.setOnAction(e -> { 
        	// Récupère les valeurs saisies par l'utilisateur
        	String email = emailField.getText();
        	String password = passwordField.getText();
        
        	// Appelle la méthode d'authentification
        	handleLogin(email, password);
        	if (MainView.getCurrentCustomer() != null) {
        		new AccountView(mainView, primaryStage);
        	}
        });
        
        // Organisation des champs dans le VBox
        existingCustomerBox.getChildren().addAll(existingLabel, instructionLabel, emailField, passwordField, forgotPasswordLink, loginButton);
        
        
        
        // Section pour les nouveaux clients (VBox droite)
        VBox newCustomerBox = new VBox(10);
        newCustomerBox.setPrefSize(600, 400);
        //AnchorPane.setTopAnchor(newCustomerBox, 200.0);
        //AnchorPane.setRightAnchor(newCustomerBox, 20.0);
        
        Label newCustomerLabel = new Label("Vous êtes nouveau client chez TennisShop");
        
        Label newCustomerInfo = new Label("En créant un compte sur notre boutique, vous pourrez passer vos commandes plus rapidement, enregistrer plusieurs adresses de livraison, consulter et suivre vos commandes, et plein d'autres choses encore.");
        newCustomerInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: normal");
        newCustomerInfo.setWrapText(true);
        
        Button createAccountButton = new Button("CRÉER UN COMPTE");
     // Gestion du clic pour afficher LoginPage
    	createAccountButton.setOnMouseClicked(event -> {
    	    new SignUpController(primaryStage);
    	   });	
        
        newCustomerBox.getChildren().addAll(newCustomerLabel, newCustomerInfo, createAccountButton);
        
        
        
        
        
        // Mise en page principale (Hbox contenant 2 Vbox)
        HBox mainBox = new HBox(20);
        mainBox.setPadding(new Insets(20));
        mainBox.setPrefSize(1200, 400);
        mainBox.getChildren().addAll(existingCustomerBox, newCustomerBox);
        mainBox.setStyle("-fx-background-color: white");
        
        VBox root = new VBox(40, mainLabel, mainBox);
        root.setPadding(new Insets(50));
        AnchorPane.setTopAnchor(root, 116.0);
        root.setPrefSize(1350, 670);
        root.setStyle("-fx-background-color: #EEEEEE");
        
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefWidth(newValue.doubleValue()); // Ajuste la largeur
        });

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefHeight(newValue.doubleValue()); // Ajuste la hauteur
        });
        
        //rootPane.getChildren().addAll(existingCustomerBox, newCustomerBox);
        rootPane.getChildren().addAll(root);
    }
    
    public static void handleLogin(String email, String password) {
    	Customer customer = authenticate(email, password);
    	
    	if (customer != null) {
    		MainView.setCurrentCustomer(customer);
    		Cart cart = CartManager.getTempCart();
    		if (cart!=null) {
    			AuthController.syncUserCart();
    			Order order = new Order(customer, customer.getCart().getItems());
    			for (CartItem item : cart.getItems()) {
    				order.decrementStock(item.getProduct().getId(), item.getSize(), item.getQuantity());
    			}
    			cart.clearCart();
    		}
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Connexion réussie");
            alert.setHeaderText(null);
            alert.setContentText("Bienvenue " + customer.getFirstName()+" !");
            alert.showAndWait(); 
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Échec de la connexion");
            alert.setHeaderText(null);
            alert.setContentText("Adresse e-mail ou mot de passe incorrect.");
            alert.showAndWait();
        }
    }

 // Méthode pour vérifier les identifiants
    public static Customer authenticate(String email, String password) {
        String query = "SELECT * FROM Customer WHERE Email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {       	
        	statement.setString(1, email); // Paramètre l'email dans la requête       	
        	ResultSet rs = statement.executeQuery();
                       
        	// Check if a user exists with this email
            while (rs.next()) {
            	String storedPassword = rs.getString("Passwords");
                if (password.equals(storedPassword))  {
                	 // Create and return a Customer object
                    String firstName = rs.getString("FirstName");
                    String lastName = rs.getString("LastName");
                    String phoneNumber = rs.getString("PhoneNumber");
                    String address = rs.getString("Address");
                    String civilityString = rs.getString("Civility");
                    String roleString = rs.getString("Role");
                    
                    // Map civility and role enums
                    Customer.Civility civility = Customer.Civility.valueOf(civilityString);
                    Customer.Role role = Customer.Role.valueOf(roleString.toUpperCase());

                    return new Customer(firstName, lastName, civility, email, phoneNumber, storedPassword, role, address);
                }
            }
        } catch (SQLException e) {e.printStackTrace();}
        
        return null; // Authentification échouée
    }
    
    public static void syncUserCart() {
    	Customer customer = MainView.getCurrentCustomer(); // Get the logged-in user
        if (customer != null) {
        	Cart userCart = customer.getCart();
        	Cart tempCart = CartManager.getTempCart();

        	// Merge the temporary cart into the user's cart
        	for (CartItem tempItem : tempCart.getItems()) {
        		userCart.addProduct(tempItem.getProduct(), tempItem.getSize(), tempItem.getQuantity());
        	}

        	// Clear the temporary cart
        	CartManager.clearTempCart();
        } else {
        	System.out.println("No user is authenticated.");
        }
    }
}

