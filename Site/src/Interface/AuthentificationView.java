package Interface;

import customer.Cart;
import customer.CartItem;
import customer.CartManager;
import customer.Customer;
import customer.Customer.Role;
import database.CustomerDAO;
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
        
        //Hyperlink forgotPasswordLink = new Hyperlink("Mot de passe oublié ?");
        //forgotPasswordLink.setOnAction(event -> MainView.showAlert("Mot de passe oublié", null, "Un mail vient de vous être envoyé sur "+ MainView.getCurrentCustomer().getEmail() + "/n"+MainView.getCurrentCustomer().getPassword(), AlertType.INFORMATION));
        
        Button loginButton = new Button("CONNEXION");
        loginButton.setOnAction(e -> { 
        	// Récupère les valeurs saisies par l'utilisateur
        	String email = emailField.getText();
        	String password = passwordField.getText();
        	
        	/*
        	if (!SignUpView.isValidEmail(email)) {
        		MainView.showAlert("Erreur", null, "L'adresse email saisie est invalide. Exemple : nom@domaine.fr", AlertType.ERROR);
        	}
            else if (!SignUpView.isValidPassword(password)) {
            	MainView.showAlert("Erreur", null, "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre, et un caractère spécial", Alert.AlertType.ERROR);
     	    }
        	*/
        	
        	// Appelle la méthode d'authentification
        	handleLogin(email, password);
        	if (MainView.getCurrentCustomer() != null) {
        		if (MainView.getCurrentCustomer().getRole()==Role.CUSTOMER)
        			new AccountView(mainView);
        		else new AdminView(mainView);       		
        	}
        });
        
        existingCustomerBox.getChildren().addAll(existingLabel, instructionLabel, emailField, passwordField, loginButton);
        
        
        
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
    
    private void handleLogin(String email, String password) {
    	Customer customer = new CustomerDAO().authenticate(email, password);
    	
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

