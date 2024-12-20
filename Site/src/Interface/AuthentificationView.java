package Interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import customer.Cart;
import customer.CartItem;
import customer.CartManager;
import customer.Customer;
import database.DatabaseConnection;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;

public class AuthentificationView {
    private AnchorPane rootPane;
    private Label mainLabel;

    public AuthentificationView(MainView mainView) {
    	// Création des labels principaux
        mainLabel = new Label("Identifiez-vous ou créez un compte");
        
    	rootPane = new AnchorPane();
        createBox(mainView);
        
        Scene authScene = new Scene(rootPane, 1350, 670);
        
        HeaderView v=new HeaderView(mainView);      
        rootPane.getChildren().addAll(v.getHeader());
        String css = this.getClass().getResource("/style.css").toExternalForm();        
        authScene.getStylesheets().add(css);
        mainView.getPrimaryStage().setScene(authScene);
    }

    private void createBox(MainView mainView) {        
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
        		new AccountView(mainView);
        	}
        });
        
        existingCustomerBox.getChildren().addAll(existingLabel, instructionLabel, emailField, passwordField, forgotPasswordLink, loginButton);
        
        
        
        // Section pour les nouveaux clients (VBox droite)
        VBox newCustomerBox = new VBox(10);
        newCustomerBox.setPrefSize(600, 400);
        
        Label newCustomerLabel = new Label("Vous êtes nouveau client chez TennisShop");
        
        Label newCustomerInfo = new Label("En créant un compte sur notre boutique, vous pourrez passer vos commandes plus rapidement, enregistrer plusieurs adresses de livraison, consulter et suivre vos commandes, et plein d'autres choses encore.");
        newCustomerInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: normal");
        newCustomerInfo.setWrapText(true);
        
        Button createAccountButton = new Button("CRÉER UN COMPTE");
        
    	createAccountButton.setOnMouseClicked(event -> {
    	    new SignUpView(mainView);
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
        
        mainView.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefWidth(newValue.doubleValue()); // Ajuste la largeur
        });

        mainView.getPrimaryStage().heightProperty().addListener((observable, oldValue, newValue) -> {
            root.setPrefHeight(newValue.doubleValue()); // Ajuste la hauteur
        });
        
        rootPane.getChildren().addAll(root);
    }
    
    public static void handleLogin(String email, String password) {
    	Customer customer = authenticate(email, password);
    	
    	if (customer != null) {
    		MainView.setCurrentCustomer(customer);
    		Cart cart = CartManager.getTempCart();
    		if (cart!=null) {
    			AuthentificationView.syncUserCart();
    		}
    		//MainView.showAlert("Connexion réussie", null, "Bienvenue " + customer.getFirstName()+" !", AlertType.INFORMATION);
        } else {
        	MainView.showAlert("Échec de la connexion", null, "Adresse e-mail ou mot de passe incorrect.", AlertType.ERROR);
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
                	int id = rs.getInt("CustomerID");
                    String firstName = rs.getString("FirstName");
                    String lastName = rs.getString("LastName");
                    String phoneNumber = rs.getString("PhoneNumber");
                    String address = rs.getString("Address");
                    String civilityString = rs.getString("Civility");
                    String roleString = rs.getString("Role");
                    
                    // Map civility and role enums
                    Customer.Civility civility = Customer.Civility.valueOf(civilityString);
                    Customer.Role role = Customer.Role.valueOf(roleString.toUpperCase());

                    return new Customer(id, firstName, lastName, civility, email, phoneNumber, storedPassword, role, address);
                }
            }
        } catch (SQLException e) {
        	MainView.showAlert("Erreur", null, "Une erreur est survenue : " + e.getMessage(), AlertType.ERROR);
        	e.printStackTrace();
        }
        
        return null; // Authentification échouée
    }
    
    public static void syncUserCart() {
    	Customer customer = MainView.getCurrentCustomer();
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

